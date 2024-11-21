package usst.spm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import usst.spm.annotation.AntiBrutePasswordExporter;
import usst.spm.dto.LoginDTO;
import usst.spm.dto.LoginDataResponseDTO;
import usst.spm.entity.Users;
import usst.spm.result.BaseResponse;
import usst.spm.result.UserSession;
import usst.spm.service.IUsersService;
import usst.spm.service.RedisService;
import usst.spm.util.HeaderUtil;

import java.util.ArrayList;

/**
 * @author jyzxc
 * @since 2024-11-21
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "用户认证接口，包括登录、注册、注销等")
@Validated
public class AuthController {
    private final IUsersService usersService;
    private final RedisService redisService;

    @PostMapping("/login")
    @Operation(summary = "用户登录接口", description = "用户登录，返回用户信息，需要在请求头中添加User-Agent，要求用户名和密码并且用户未登录")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "登录成功", content = @Content(schema = @Schema(implementation = LoginDataResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "已经登录或者请求参数错误"),
            @ApiResponse(responseCode = "401", description = "用户名或密码错误")
    })
    @AntiBrutePasswordExporter(maxTry = 10, waitTime = 100)
    public BaseResponse login(
            @Valid @RequestBody LoginDTO loginRequestDTO,
            HttpSession session,
            @RequestHeader("User-Agent") @Schema(description = "用户登录请求头") String userAgent,
            HttpServletRequest request
    ) {
        if (session.getAttribute("userSession") != null) {
            return new BaseResponse(400, "您已经登陆了");
        }
        String username = loginRequestDTO.getUsername();
        String password = loginRequestDTO.getPassword();
        Users user = usersService.login(username, password);
        if (user == null) {
            return new BaseResponse(401, "用户名或密码错误");
        }
        setRedis(user.getId(), session, userAgent, request);
        return new LoginDataResponseDTO(session.getId(), user);
    }

    // 将用户信息存入redis
    private void setRedis(Integer userId, HttpSession session, String userAgent, HttpServletRequest request) {
        UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        userSession.setSessionId(session.getId());
        userSession.setLoginIp(HeaderUtil.getClientIp(request));
        userSession.setLoginUserAgent(userAgent);
        userSession.setLoginTime(System.currentTimeMillis());
        session.setAttribute("userSession", userSession);

        String key = "app:session:user:" + userId;

        ArrayList<String> sessionIds = (ArrayList<String>) redisService.getValue(key);
        if (sessionIds != null) {
            if (!sessionIds.contains(session.getId())) {
                sessionIds.add(session.getId());
            }
        } else {
            sessionIds = new ArrayList<>();
            sessionIds.add(session.getId());
        }
        redisService.setValue(key, sessionIds);
    }
}
