package usst.spm.expression;

import jakarta.annotation.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import usst.spm.entity.Questions;
import usst.spm.entity.UserLogin;
import usst.spm.service.ICoursesService;
import usst.spm.service.IQuestionsService;

/**
 * @author jyzxc
 * @since 2024-12-18
 */
@Component("QuestionsExpression")
public class QuestionsExpression {
    @Resource
    IQuestionsService questionsService;
    @Resource
    ICoursesService coursesService;
    public boolean canEditQuestions(Integer questionId) {
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(users.getRoles().contains("super-admin"))
        {
            return true;
        }
        if(!users.isAdmin())
        {
            throw new AccessDeniedException("只有老师和管理员有权限访问");
        }
        Questions questions = questionsService.getById(questionId);
        Integer courseId = questions.getCourseId();
        if(!coursesService.userInCourse(courseId, (Integer) users.getUserId()))
        {
            throw new AccessDeniedException("你要先加入课程才能访问");
        }
        return true;
    }
}
