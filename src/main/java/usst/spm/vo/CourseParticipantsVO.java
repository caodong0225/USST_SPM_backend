package usst.spm.vo;

import lombok.Getter;
import lombok.Setter;
import usst.spm.entity.CourseParticipants;
import usst.spm.entity.Users;

/**
 * @author jyzxc
 * @since 2024-11-28
 */
@Getter
@Setter
public class CourseParticipantsVO {
    CourseParticipants courseParticipants;
    Users users;
}
