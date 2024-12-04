package usst.spm.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
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
    private String coursePic;

    /**
     * 课程开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 课程结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
