package usst.spm.controller;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author jyzxc
 * @since 2024-11-21
 */
@Getter
@Setter
@Schema(description = "注册的post请求体")
public class RegisterRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    @Schema(name = "username", description = "用户名,长度必须在3-20之间,只能包含字母、数字和下划线", example = "admin")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 32, message = "密码长度必须在6-32之间")
    @Pattern(message = "密码必须包含大小写字母、数字或特殊字符其中至少两种", regexp = "^(?![a-zA-Z]+$)(?!\\d+$)(?![^\\da-zA-Z\\s]+$).{6,32}$")
    @Schema(name = "password", description = "密码,长度必须在6-32之间,必须包含大小写字母、数字或特殊字符其中至少两种", example = "Admin123")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @Size(min = 1, max = 20, message = "昵称长度必须在1-20之间")
    @Schema(name = "nickname", description = "逆臣", example = "小明")
    private String nickname;

    @NotBlank(message = "邮箱不能为空")
    @Schema(name = "email", description = "邮箱", example = "test@qq.com")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式不正确")
    private String email;
}
