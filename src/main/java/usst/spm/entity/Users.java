package usst.spm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Data
@Schema(description = "用户信息")
public class Users implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "用户ID", example = "1")
    private Integer id;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", example = "1720524833000L")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", example = "1720524833000L")
    private LocalDateTime updatedAt;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 昵称
     */
    @Schema(description = "昵称", example = "管理员")
    private String nickname;

    /**
     * 邮箱
     */
    @Schema(description = "邮箱", example = "2235062001@st.usst.edu.cn")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$", message = "邮箱格式错误")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "13621868528")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式错误")
    private String phone;

    /**
     * 密码哈希
     */
    @Schema(description = "密码的哈希，为敏感数据，不显示")
    @JsonIgnore
    private String hash;

}
