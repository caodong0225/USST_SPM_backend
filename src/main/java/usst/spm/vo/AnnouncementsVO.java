package usst.spm.vo;

import lombok.Getter;
import lombok.Setter;
import usst.spm.entity.Announcements;
import usst.spm.entity.Courses;
import usst.spm.entity.Users;

/**
 * @author jyzxc
 * @since 2024-12-1
 */
@Getter
@Setter
public class AnnouncementsVO {
    private Announcements announcement;
    private Courses course;
    private Users user;
}
