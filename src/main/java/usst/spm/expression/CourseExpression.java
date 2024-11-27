package usst.spm.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import usst.spm.entity.UserLogin;
import usst.spm.service.ICoursesService;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Component("CourseExpression")
@RequiredArgsConstructor
public class CourseExpression {
    private ICoursesService coursesService;
    public boolean canAccessCourse(Integer courseId) {
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(users.isAdmin())
        {
            return true;
        }
        return coursesService.userInCourse(courseId, (Integer) users.getUserId());
    }
}
