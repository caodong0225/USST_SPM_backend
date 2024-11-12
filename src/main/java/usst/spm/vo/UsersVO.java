package usst.spm.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
public class UsersVO {
    @Schema(description = "用户ID")
    private Long id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "昵称")
    private String nickname;
    @Schema(description = "邮箱")
    private String email;
    @Schema(description = "角色名称")
    private String roleName; // 角色名称
}