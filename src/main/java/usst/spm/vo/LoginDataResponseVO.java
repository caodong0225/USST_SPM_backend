package usst.spm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import usst.spm.entity.Users;

/**
 * @author jyzxc
 * @since 2024-11-21
 */
@Getter
@Setter
@Schema(description = "登录成功后的响应体")
public class LoginDataResponseVO {
    private Users users;
    private String[] roles;
    private String sessionId;
}