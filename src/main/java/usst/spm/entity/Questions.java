package usst.spm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 问题列表
 * @TableName questions
 */
@TableName(value ="questions")
@Data
public class Questions implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 问题类型
     */
    @TableField(value = "question_type")
    private String questionType;

    /**
     * 问题题面
     */
    @TableField(value = "question_name")
    private String questionName;

    /**
     * 问题难度级别
     */
    @TableField(value = "question_level")
    private String questionLevel;

    /**
     * 问题选项
     */
    @TableField(value = "question_options")
    private String questionOptions;

    /**
     * 创建时间
     */
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 所属课程
     */
    @TableField(value = "course_id")
    private Integer courseId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}