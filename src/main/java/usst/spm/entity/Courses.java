package usst.spm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 所开设课程
 * @author jyzxc
 * @TableName courses
 */
@TableName(value ="courses")
@Data
public class Courses implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程名称
     */
    @TableField(value = "course_name")
    private String courseName;

    /**
     * 课程描述
     */
    @TableField(value = "course_desc")
    private String courseDesc;

    /**
     * 课程图片
     */
    @TableField(value = "course_pic")
    private Integer coursePic;

    /**
     * 课程开始时间
     */
    @TableField(value = "start_time")
    private LocalDateTime startTime;

    /**
     * 课程结束时间
     */
    @TableField(value = "end_time")
    private LocalDateTime endTime;

    /**
     * 课程当前状态
     */
    @TableField(value = "status")
    private String status;

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
     * 开设该课程的用户
     */
    @TableField(value = "user_id")
    private Integer userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}