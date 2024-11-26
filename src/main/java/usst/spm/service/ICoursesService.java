package usst.spm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.Courses;

/**
* @author jyzxc
* @description 针对表【courses(所开设课程)】的数据库操作Service
* @createDate 2024-11-25 23:26:18
*/
public interface ICoursesService extends IService<Courses> {
    boolean createCourse(Courses course);
    boolean updateCourse(Courses course);
}
