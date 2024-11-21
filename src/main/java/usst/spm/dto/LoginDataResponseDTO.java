package usst.spm.dto;

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
public class LoginDataResponseDTO extends BaseResponse {
    @Schema(description = "会话ID", example = "bad5684c-31c3-469a-adc8-8e5361c1b27b")
    private String sessionId;
    private Users user;

    public LoginDataResponseDTO() {
        super();
    }

    public LoginDataResponseDTO(String sessionId, Users user) {
        super();
        this.sessionId = sessionId;
        this.user = user;
    }
}