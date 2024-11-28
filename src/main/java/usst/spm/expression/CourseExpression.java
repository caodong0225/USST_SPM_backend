package usst.spm.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import usst.spm.entity.UserLogin;
import usst.spm.service.ICoursesService;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Component("CourseExpression")
public class CourseExpression {
    private final ICoursesService coursesService;
    public CourseExpression(ICoursesService coursesService) {
        this.coursesService = coursesService;
    }
    public boolean canAccessCourse(Integer courseId) {
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(users.isAdmin())
        {
            return true;
        }
        if(!coursesService.userInCourse(courseId, (Integer) users.getUserId()))
        {
            throw new AccessDeniedException("你要先加入课程才能访问");
        }
        return true;
    }
    public boolean canEditCourse(Integer courseId) {
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(users.getRoles().contains("super-admin"))
        {
            return true;
        }
        if(!users.isAdmin())
        {
            throw new AccessDeniedException("只有老师和管理员有权限访问");
        }
        if(!coursesService.userInCourse(courseId, (Integer) users.getUserId()))
        {
            throw new AccessDeniedException("你要先加入课程才能编辑");
        }
        return coursesService.userInCourse(courseId, (Integer) users.getUserId());
    }
}
