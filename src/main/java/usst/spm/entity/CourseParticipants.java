package usst.spm.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Getter
@Setter
@TableName("course_participants")
public class CourseParticipants implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer courseId;

    private Integer userId;

    private String status;

    private String comment;

}
