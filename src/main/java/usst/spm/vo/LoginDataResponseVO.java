package usst.spm.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import usst.spm.entity.Users;
import usst.spm.result.BaseResponse;

/**
 * @author jyzxc
 * @since 2024-11-21
 */
@Getter
@Setter
@Schema(description = "登录成功后的响应体")
public class LoginDataResponseVO extends BaseResponse {
    Users users;
    String[] roles;
    String sessionId;
}