package usst.spm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.CourseParticipants;
import usst.spm.entity.Users;
import usst.spm.vo.CourseParticipantsVO;
import usst.spm.vo.UserCoursesVO;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
public interface ICourseParticipantsService extends IService<CourseParticipants> {
    IPage<CourseParticipantsVO> getParticipantsPage(Page<Object> objectPage, Integer courseId, QueryWrapper<Users> queryWrapper);

    IPage<UserCoursesVO> getUserCoursesPage(Page<Object> objectPage, Integer userId);
    boolean insertCourseParticipant(Integer courseId, Integer userId);
}
