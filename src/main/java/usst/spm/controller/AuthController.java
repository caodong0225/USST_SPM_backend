package usst.spm.controller;

import com.alibaba.fastjson.JSON;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import usst.spm.annotation.AntiBrutePasswordExporter;
import usst.spm.annotation.PreventDuplicateSubmit;
import usst.spm.dto.LoginDTO;
import usst.spm.result.GeneralDataResponse;
import usst.spm.vo.LoginDataResponseVO;
import usst.spm.entity.UserLogin;
import usst.spm.entity.Users;
import usst.spm.result.BaseDataResponse;
import usst.spm.result.BaseResponse;
import usst.spm.result.UserSession;
import usst.spm.service.IUsersService;
import usst.spm.service.RedisService;
import usst.spm.util.HeaderUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            @ApiResponse(responseCode = "200", description = "登录成功", content = @Content(schema = @Schema(implementation = LoginDataResponseVO.class))),
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
        return new LoginDataResponseVO(session.getId(), user);
    }

    @GetMapping("/session/list")
    @Operation(summary = "获取用户会话列表", description = "获取用户的会话列表，需要用户已登录")
    @ApiResponse(responseCode = "200", description = "获取成功")
    @ApiResponse(responseCode = "403", description = "未登录", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    public BaseDataResponse getSessionList() {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String key = "app:session:user:" + user.getUserId();
        ArrayList<String> sessionIds = (ArrayList<String>) redisService.getValue(key);
        if (sessionIds == null) {
            sessionIds = new ArrayList<>();
        }

        Map<String, Object> data = new HashMap<>();
        ArrayList<String> validSessionIds = new ArrayList<>();

        for (String sessionId : sessionIds) {
            Object userSessionTemp = redisService.getHashValue(
                    "spring:session:sessions:" + sessionId,
                    "sessionAttr:userSession"
            );
            UserSession userSession = JSON.parseObject((String) userSessionTemp, UserSession.class);
            if (userSession != null) {
                data.put(sessionId, userSession);
                validSessionIds.add(sessionId);
            }
        }
        redisService.setValue(key, validSessionIds);

        return new BaseDataResponse(data);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户注销", description = "注销用户，需要用户已登录")
    @ApiResponse(responseCode = "200", description = "注销成功")
    @ApiResponse(responseCode = "403", description = "未登录", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    @ApiResponse(responseCode = "500", description = "注销失败")
    public BaseDataResponse logout(HttpSession session) {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try {
            removeSession(user.getUserId(), user.getSessionId());
            session.invalidate();
            //注销spring security的认证
            SecurityContextHolder.clearContext();
            return new BaseDataResponse();
        } catch (Exception e) {
            return new BaseDataResponse(500, "Failed to logout: " + e.getMessage());
        }
    }

    @PostMapping("/register")
    @PreventDuplicateSubmit // 防止接口抖动问题
    @Operation(summary = "用户注册", description = "用户注册，需要用户名、密码、昵称和邮箱，返回用户注册后的信息，要求用户未登录")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "注册成功"),
            @ApiResponse(responseCode = "400", description = "注册失败"),
            @ApiResponse(responseCode = "403", description = "已经登录")})
    public GeneralDataResponse<Users> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO,
            HttpSession session
    ) {
        if (session.getAttribute("userSession") != null) {
            return new GeneralDataResponse<>(403, "您已经登陆了");
        }

        if (usersService.canAddUser(
                registerRequestDTO.getUsername(),
                registerRequestDTO.getEmail()
        )) {
            return new GeneralDataResponse<>(400, "用户名或邮箱已存在");
        }

        Users user = new Users();
        user.setHash(registerRequestDTO.getPassword());
        user.setEmail(registerRequestDTO.getEmail());
        user.setUsername(registerRequestDTO.getUsername());
        user.setNickname(registerRequestDTO.getUsername());
        user = usersService.createUser(user);
        return new GeneralDataResponse<>(user);
    }

    // 从redis中删除用户会话
    private void removeSession(Serializable userId, String sessionId) {
        String key = "app:session:user:" + userId;
        ArrayList<String> sessionIds = (ArrayList<String>) redisService.getValue(key);
        if (!sessionIds.contains(sessionId)) {
            return;
        }
        sessionIds.remove(sessionId);
        redisService.setValue(key, sessionIds);
        redisService.removeHashValue("spring:session:sessions:" + sessionId, "sessionAttr:userSession");
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
