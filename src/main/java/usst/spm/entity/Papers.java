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
 * 比赛表
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
@Getter
@Setter
@Schema(description = "比赛表")
public class Papers implements Serializable {

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
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;

    /**
     * 考试名称
     */
    @Schema(description = "考试名称")
    private String paperName;

    /**
     * 考试描述
     */
    @Schema(description = "考试描述")
    private String paperDesc;

    /**
     * 考试开始时间
     */
    @Schema(description = "考试开始时间",example = "2024-08-15T08:00:00")
    private LocalDateTime paperStartTime;

    /**
     * 考试结束时间
     */
    @Schema(description = "考试结束时间",example = "2024-08-15T16:00:00")
    private LocalDateTime paperEndTime;

    /**
     * 考试状态
     */
    @Schema(description = "考试状态，只允许为ongoing, upcoming, ended",example = "ongoing")
    private String status;

    /**
     * 可见性
     */
    @Schema(description = "可见性",example = "true")
    private Boolean visible;
}
