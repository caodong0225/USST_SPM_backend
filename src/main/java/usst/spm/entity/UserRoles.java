package usst.spm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Getter
@Setter
@TableName("user_roles")
@Schema(description = "用户角色权限表")
public class UserRoles implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键")
    private Integer id;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间" ,type = "string", format = "date-time")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间" ,type = "string", format = "date-time")
    private LocalDateTime updatedAt;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Integer userId;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "admin")
    private String roleName;
}
