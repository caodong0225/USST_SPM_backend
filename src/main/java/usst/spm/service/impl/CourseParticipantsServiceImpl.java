package usst.spm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.CourseParticipants;
import usst.spm.entity.Users;
import usst.spm.mapper.CourseParticipantsMapper;
import usst.spm.service.ICourseParticipantsService;
import usst.spm.vo.CourseParticipantsVO;
import usst.spm.vo.UserCoursesVO;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
@Service
public class CourseParticipantsServiceImpl extends ServiceImpl<CourseParticipantsMapper, CourseParticipants> implements ICourseParticipantsService {

    @Override
    public IPage<CourseParticipantsVO> getParticipantsPage(Page<Object> objectPage, Integer courseId, QueryWrapper<Users> queryWrapper) {
        return this.baseMapper.getParticipantsPage(objectPage, courseId, queryWrapper);
    }

    @Override
    public IPage<UserCoursesVO> getUserCoursesPage(Page<Object> objectPage, Integer userId) {
        return this.baseMapper.getUserCoursesPage(objectPage, userId);
    }

    @Override
    public boolean insertCourseParticipant(Integer courseId, Integer userId) {
        CourseParticipants courseParticipants = new CourseParticipants();
        courseParticipants.setCourseId(courseId);
        courseParticipants.setUserId(userId);
        return this.save(courseParticipants);
    }
}
