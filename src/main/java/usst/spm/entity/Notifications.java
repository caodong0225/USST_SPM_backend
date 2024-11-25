package usst.spm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 通知表
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
@Getter
@Setter
public class Notifications implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "消息Id", type = "integer")
    private Integer id;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    /**
     * 标题
     */
    @Schema(description = "标题", type = "string")
    private String title;

    /**
     * 内容
     */
    @Schema(description = "内容", type = "string")
    private String content;

    /**
     * 发布者用户ID
     */
    @Schema(description = "发布者用户ID",type = "integer")
    private Integer userId;

    /**
     * 是否已读
     */
    @Schema(description = "是否已读", type = "boolean")
    private Boolean isRead;
}
