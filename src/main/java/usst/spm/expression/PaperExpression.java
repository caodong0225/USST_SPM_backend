package usst.spm.expression;

import jakarta.annotation.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import usst.spm.entity.Papers;
import usst.spm.entity.UserLogin;
import usst.spm.service.ICourseParticipantsService;
import usst.spm.service.ICoursesService;
import usst.spm.service.IPapersService;

/**
 * @author jyzxc
 * @since 2024-12-23
 */
@Component("PaperExpression")
public class PaperExpression {
    @Resource
    IPapersService papersService;
    @Resource
    ICoursesService coursesService;
    @Resource
    ICourseParticipantsService courseParticipantsService;
    public boolean canEditPaper(Integer paperId) {
        Papers paper = papersService.getById(paperId);
        if(paper == null) {
             throw new IllegalArgumentException("试卷不存在");
        }
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(users.getRoles().contains("super-admin"))
        {
            return true;
        }
        if(!users.isAdmin())
        {
            throw new AccessDeniedException("只有老师和管理员有权限访问");
        }
        if(coursesService.getById(paper.getCourseId()).getUserId() != users.getUserId())
        {
            throw new AccessDeniedException("你不是该课程的创建者");
        }
        return true;
    }
    public boolean canAccessPaper(Integer paperId) {
        Papers paper = papersService.getById(paperId);
        if(paper == null) {
            throw new IllegalArgumentException("试卷不存在");
        }
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(users.getRoles().contains("super-admin"))
        {
            return true;
        }
        if(!courseParticipantsService.hasCourseParticipant(paper.getCourseId(), (Integer) users.getUserId()))
        {
            throw new AccessDeniedException("你不是该课程的学生");
        }
        return true;
    }
}
