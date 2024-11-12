package usst.spm.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Getter
@Setter
@Schema(description = "用户的全部信息，包括用户信息、角色信息、额外信息")
public class UserInfo implements Serializable {
    @Schema(description = "用户的基本信息")
    private Users user;
    @Schema(description = "用户的角色信息")
    private List<UserRoles> userRoles;
    @Schema(description = "用户的额外信息")
    private Map<String, Object> userExtraInfo;
}