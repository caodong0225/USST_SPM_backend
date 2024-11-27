package usst.spm.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Getter
@Setter
public class CreateCourseDTO {
    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程描述
     */
    private String courseDesc;

    /**
     * 课程图片
     */
    private Integer coursePic;

    /**
     * 课程开始时间
     */
    private LocalDateTime startTime;

    /**
     * 课程结束时间
     */
    private LocalDateTime endTime;
}
