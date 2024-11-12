package usst.spm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Setter
@Getter
@Schema(name = "UpdateRoleRequestDTO", description = "更新角色身份请求")
public class UpdateRoleRequestDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(name = "roles", description = "角色列表", example = "[\"admin\"]", requiredMode = REQUIRED)
    private List<String> roles;
}
