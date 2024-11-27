package usst.spm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import usst.spm.dto.CreateCourseDTO;
import usst.spm.entity.Courses;
import usst.spm.entity.Users;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.ICoursesService;
import usst.spm.service.IUsersService;
import usst.spm.vo.CourseInfoVO;

import java.time.LocalDateTime;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
@RequestMapping("/course")
@Tag(name = "课程管理接口")
@Validated
@RequiredArgsConstructor
public class CoursesController {
    private ICoursesService coursesService;
    private IUsersService usersService;

    @PostMapping("/add")
    @Operation(summary = "添加课程")
    @PreAuthorize("@AuthExpression.isTeacher()")
    public BaseResponse addCourse(
            CreateCourseDTO createCourseDTO
    ) {
        Courses course = new Courses();
        if(createCourseDTO.getStartTime() == null || createCourseDTO.getEndTime() == null) {
            throw new IllegalArgumentException("开始时间和结束时间不能为空");
        }
        // 如果开始时间在结束时间之后
        if(createCourseDTO.getStartTime().isAfter(createCourseDTO.getEndTime())) {
            throw new IllegalArgumentException("开始时间不能在结束时间之后");
        }
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        course.setCourseName(createCourseDTO.getCourseName());
        course.setCourseDesc(createCourseDTO.getCourseDesc());
        course.setStartTime(createCourseDTO.getStartTime());
        course.setEndTime(createCourseDTO.getEndTime());
        course.setStatus(now.isBefore(createCourseDTO.getStartTime()) ? "upcoming" : now.isAfter(createCourseDTO.getEndTime()) ? "ended" : "ongoing");
        coursesService.save(course);
        // TODO: 添加spring quartz定时任务
        return BaseResponse.makeResponse(200, "添加成功");
    }

    @GetMapping("/get/{id}")
    @Operation(summary = "通过课程id获取课程信息")
    @PreAuthorize("@CourseExpression.canAccessCourse(#id)")
    public GeneralDataResponse<CourseInfoVO> getCourseById(
            @Min(value = 0, message = "课程id必须大于0") @PathVariable Integer id) {
        Courses course = coursesService.getById(id);
        if(course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        Users teacher = usersService.getById(course.getUserId());
        CourseInfoVO courseInfoVO = new CourseInfoVO();
        courseInfoVO.setCourse(course);
        courseInfoVO.setTeacher(teacher);
        return new GeneralDataResponse<>(courseInfoVO);
    }

}
