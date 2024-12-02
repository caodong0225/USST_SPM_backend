package usst.spm.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author jyzxc
 * @since 2024-11-28
 */
@Getter
@Setter
public class UserCoursesVO {
    private Integer courseId;
    private String courseName;
    private String courseDesc;
    private String coursePic;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String participantComment;
}
