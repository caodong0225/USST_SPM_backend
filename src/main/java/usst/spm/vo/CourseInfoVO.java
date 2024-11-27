package usst.spm.vo;

import lombok.Getter;
import lombok.Setter;
import usst.spm.entity.Courses;
import usst.spm.entity.Users;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Setter
@Getter
public class CourseInfoVO {
    private Courses course;
    private Users teacher;
}
