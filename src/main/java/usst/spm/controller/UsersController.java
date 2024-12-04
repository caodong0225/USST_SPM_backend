package usst.spm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import usst.spm.annotation.MyAllowedExtKey;
import usst.spm.dto.UpdateExtraRequestDTO;
import usst.spm.dto.UpdateRoleRequestDTO;
import usst.spm.entity.UserInfo;
import usst.spm.entity.UserLogin;
import usst.spm.entity.Users;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.IUserExtsService;
import usst.spm.service.IUsersService;
import usst.spm.service.MinioService;
import usst.spm.vo.UsersVO;

import java.util.List;
import java.util.Map;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口，除了/user/list和/user/get/{id}外，其他接口都需要登录。而/user/change/**的接口都需要管理员权限。")
public class UsersController {
    private final IUsersService usersService;
    // 允许查找的排序选项
    private static final String[] ALLOWED_SORTED_FIELDS = new String[]{
            "id",
            "username",
            "nickname",
            "email",
            "created_at",
            "updated_at"
    };

    // 允许的角色名
    private static final String[] ALLOWED_ROLES = new String[]{
            "teacher",
    };

    @Resource
    private MinioService minioService;

    private static final String DIRECTORY_NAME = "user";

    @GetMapping("/list")
    @Schema(description = "获取用户列表")
    @Operation(summary = "获取用户列表", description = "获取用户列表，无需登录")
    @Parameters({
            @Parameter(name = "current", description = "当前页数，默认为1，不能小于1"),
            @Parameter(name = "size", description = "每页数量，默认为10，范围在1~100之间"),
            @Parameter(name = "sort", description = "排序字段，默认为id，可选值有id, username, nickname, email, created_at, updated_at"),
            @Parameter(name = "order", description = "排序方式，只能是升序asc和降序desc，默认为asc"),
            @Parameter(name = "filter", description = "筛选条件，非必须，可以根据用户名、昵称、邮箱进行筛选")
    })
    @ApiResponse(responseCode = "200", description = "成功")
    @ApiResponse(responseCode = "400", description = "参数错误", content = @Content(schema = @Schema(implementation = BaseResponse.class)))
    public ResponseEntity<GeneralDataResponse<IPage<UsersVO>>> getUsers(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "当前页最小为1") int current,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100, message = "size范围必须在1~100之间") int size,
            @RequestParam(defaultValue = "id") @Pattern(
                    regexp = "id|username|nickname|email|created_at|updated_at",
                    message = "sort只能是id, username, nickname, email, created_at, updated_at") String sort,
            @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc", message = "order只能是asc或者desc") String order,
            @RequestParam(required = false) String filter
    ) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        // 添加筛选条件
        if (filter != null && !filter.isEmpty()) {
            queryWrapper.like("u.username", filter)
                    .or().like("u.nickname", filter)
                    .or().like("u.email", filter);
        }
        // 添加排序条件
        if ("asc".equalsIgnoreCase(order)) {
            queryWrapper.orderByAsc("u." + sort);
        } else {
            queryWrapper.orderByDesc("u." + sort);
        }
        // 分页查询
        IPage<UsersVO> usersPage = usersService.selectUsersWithRoles(new Page<>(current, size), queryWrapper);
        return ResponseEntity.ok(new GeneralDataResponse<>(usersPage));
    }

    @Operation(summary = "获取用户信息", description = "获取当前登录用户的信息")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "成功"),
            @ApiResponse(responseCode = "403", description = "未登录", content = @Content(schema = @Schema(implementation = BaseResponse.class)))})
    @GetMapping("/me")
    public GeneralDataResponse<UserInfo> getUserInfo() {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new GeneralDataResponse<>(usersService.getUserInfo(user.getUserId()));
    }

    @Parameters(value = {
            @Parameter(name = "id", description = "用户的id值，必须大于等于0，为必填项")
    })
    @Operation(summary = "获取用户信息", description = "根据用户ID获取用户信息，无需登录，返回用户信息")
    @ApiResponse(responseCode = "404", description = "用户未找到")
    @ApiResponse(responseCode = "200", description = "成功")
    @GetMapping("/get/{id}")
    public GeneralDataResponse<UserInfo> getUserInfoById(
            @Min(value = 0, message = "用户id必须大于0") @PathVariable Long id
    ) {
        UserInfo userInfo = usersService.getUserInfo(id);
        if (userInfo == null) {
            return new GeneralDataResponse<>(404, "用户未找到");
        }
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // 只有管理员或者是自己才能看到完整信息
            if (user.isAdmin() || user.getUserId().equals(id)) {
                return new GeneralDataResponse<>(userInfo);
            }
        }
        // TODO: 脱敏处理，过滤掉敏感信息，
        Map<String, Object> userExtraInfo = userInfo.getUserExtraInfo();
        userInfo.setUserExtraInfo(userExtraInfo);
        return new GeneralDataResponse<>(userInfo);
    }

    @PutMapping("/change/{id}/role")
    @Operation(summary = "修改用户角色", description = "修改用户角色，只有超级管理员可以修改，不能修改自己的角色信息，可选值有teacher")
    @Parameter(name = "id", description = "用户的id值，必须大于等于0，为必填项")
    @ApiResponse(responseCode = "200", description = "成功")
    @ApiResponse(responseCode = "400", description = "参数错误，角色不允许")
    @PreAuthorize("@UsersExpression.isUserExist(#id) AND @UsersExpression.isUserSame(#id) AND @AuthExpression.isSuperAdmin()")
    public BaseResponse updateUserRole(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateRoleRequestDTO updateRoleRequestDTO
    ) {
        List<String> updateRoles = updateRoleRequestDTO.getRoles();
        if(updateRoles == null || updateRoles.isEmpty()) {
            return BaseResponse.makeResponse(400, "Roles cannot be empty");
        }
        for (String role : updateRoles) {
            if (!List.of(ALLOWED_ROLES).contains(role)) {
                return BaseResponse.makeResponse(400, "Role " + role + " is not allowed");
            }
        }
        try {
            usersService.updateUserRoles(id, updateRoles);
            return BaseResponse.makeResponse(200);
        } catch (Exception e) {
            return BaseResponse.makeResponse(500, e.getMessage());
        }
    }

    @PutMapping("/change/{id}/ext/{key}")
    @Operation(summary = "修改用户拓展信息", description = "修改用户拓展信息，只有超级管理员可以修改，不能修改自己的拓展信息")
    @Parameter(name = "id", description = "用户的id值，必须大于等于0，为必填项")
    @Parameter(name = "key", description = "拓展信息的key值，必须是合法的key值")
    @ApiResponse(responseCode = "200", description = "成功")
    @ApiResponse(responseCode = "400", description = "参数错误")
    @ApiResponse(responseCode = "500", description = "失败")
    @PreAuthorize("@UsersExpression.isUserExist(#id) AND @AuthExpression.isSuperAdmin()")
    public BaseResponse updateUserExt(
            @PathVariable Integer id,
            @PathVariable @MyAllowedExtKey String key,
            @RequestBody UpdateExtraRequestDTO value
    ) {
        try {
            if("pic".equals(key))
            {
                try {
                    value.setValue(minioService.setPictureUri(value.getValue(),DIRECTORY_NAME));
                } catch (Exception e) {
                    return BaseResponse.makeResponse(500, e.getMessage());
                }
            }
            usersService.updateUserExt(id, key, value.getValue());
            return BaseResponse.makeResponse(200);
        } catch (Exception e) {
            return BaseResponse.makeResponse(500, e.getMessage());
        }
    }

    @DeleteMapping("/change/{id}/ext/{key}")
    @Operation(summary = "删除用户拓展信息", description = "删除用户拓展信息，只有超级管理员可以删除，不能删除自己的拓展信息")
    @Parameter(name = "id", description = "用户的id值，必须大于等于0，为必填项")
    @Parameter(name = "key", description = "拓展信息的key值，必须是合法的key值")
    @ApiResponse(responseCode = "200", description = "成功")
    @ApiResponse(responseCode = "400", description = "参数错误")
    @ApiResponse(responseCode = "500", description = "失败")
    @PreAuthorize("@UsersExpression.isUserExist(#id) AND @AuthExpression.isSuperAdmin()")
    public BaseResponse deleteUserExt(
            @PathVariable Integer id,
            @PathVariable @MyAllowedExtKey String key
    ) {
        try {
            usersService.removeUserExt(id, key);
            return BaseResponse.makeResponse(200);
        } catch (Exception e) {
            return BaseResponse.makeResponse(500, e.getMessage());
        }
    }

    @DeleteMapping("/change/{id}/role")
    @Operation(summary = "删除用户角色", description = "删除用户角色，只有超级管理员可以删除，不能删除自己的角色信息")
    @Parameter(name = "id", description = "用户的id值，必须大于等于0，为必填项")
    @ApiResponse(responseCode = "200", description = "成功")
    @ApiResponse(responseCode = "400", description = "参数错误")
    @ApiResponse(responseCode = "500", description = "失败")
    @PreAuthorize("@UsersExpression.isUserExist(#id) AND @UsersExpression.isUserSame(#id) AND @AuthExpression.isSuperAdmin()")
    public BaseResponse deleteUserRole(@PathVariable Integer id) {
        try {
            usersService.removeUserRoles(id);
            return BaseResponse.makeResponse(200);
        } catch (Exception e) {
            return BaseResponse.makeResponse(500, e.getMessage());
        }
    }

    @DeleteMapping("/change/{id}")
    @Operation(summary = "删除用户", description = "删除用户，只有超级管理员可以删除，不能删除自己")
    @Parameter(name = "id", description = "用户的id值，必须大于等于0，为必填项")
    @ApiResponse(responseCode = "200", description = "成功")
    @ApiResponse(responseCode = "400", description = "参数错误")
    @ApiResponse(responseCode = "500", description = "失败")
    @PreAuthorize("@UsersExpression.isUserExist(#id) AND @UsersExpression.isUserSame(#id) AND @AuthExpression.isSuperAdmin()")
    public BaseResponse deleteUser(@PathVariable Integer id) {
        try {
            usersService.removeUserById(id);
            return BaseResponse.makeResponse(200);
        } catch (Exception e) {
            return BaseResponse.makeResponse(500, e.getMessage());
        }
    }

}
