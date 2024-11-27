package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.CourseParticipants;
import usst.spm.entity.Courses;
import usst.spm.mapper.CoursesMapper;
import usst.spm.service.ICourseParticipantsService;
import usst.spm.service.ICoursesService;

/**
* @author jyzxc
* @description 针对表【courses(所开设课程)】的数据库操作Service实现
* @createDate 2024-11-25 23:26:18
*/
@Service
public class CoursesServiceImpl extends ServiceImpl<CoursesMapper, Courses> implements ICoursesService {

    private final ICourseParticipantsService courseParticipantsService;


    public CoursesServiceImpl(ICourseParticipantsService courseParticipantsService) {
        this.courseParticipantsService = courseParticipantsService;
    }

    @Override
    public boolean createCourse(Courses course) {
        return this.save(course);
    }

    @Override
    public boolean updateCourse(Courses course) {
        return this.updateById(course);
    }

    @Override
    public boolean userInCourse(Integer courseId, Integer userId) {
        return courseParticipantsService.lambdaQuery()
                .eq(CourseParticipants::getCourseId, courseId)
                .eq(CourseParticipants::getUserId, userId)
                .count() > 0;
    }
}




