package usst.spm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import usst.spm.dto.CreateCourseDTO;
import usst.spm.entity.Courses;
import usst.spm.entity.UserLogin;
import usst.spm.entity.Users;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.ICourseParticipantsService;
import usst.spm.service.ICoursesService;
import usst.spm.service.IUsersService;
import usst.spm.vo.CourseInfoVO;
import usst.spm.vo.CourseParticipantsVO;
import usst.spm.vo.UserCoursesVO;

import java.time.LocalDateTime;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
@RequestMapping("/course")
@Tag(name = "课程管理接口")
@Validated
@RequiredArgsConstructor
@RestController
public class CoursesController {
    private final ICoursesService coursesService;
    private final IUsersService usersService;
    private final ICourseParticipantsService courseParticipantsService;

    @GetMapping("/participants/list/{id}")
    @Operation(summary = "获取课程参与者列表")
    @PreAuthorize("@CourseExpression.canAccessCourse(#id)")
    public GeneralDataResponse<IPage<CourseParticipantsVO>> getParticipants(
            @Min(value = 0, message = "课程id必须大于0") @PathVariable Integer id,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "当前页最小为1") int current,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100, message = "size范围必须在1~100之间") int size,
            @RequestParam(defaultValue = "id") @Pattern(
                    regexp = "id|username|nickname|email|created_at|updated_at",
                    message = "sort只能是id, username, nickname, email, created_at, updated_at") String sort,
            @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc", message = "order只能是asc或者desc") String order,
            @RequestParam(required = false) String filter
    ) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        // 添加筛选条件
        if (filter != null && !filter.isEmpty()) {
            queryWrapper.like("u.username", filter)
                    .or().like("u.nickname", filter)
                    .or().like("u.email", filter);
        }
        // 添加排序条件
        if ("asc".equalsIgnoreCase(order)) {
            queryWrapper.orderByAsc("u."+sort);
        } else {
            queryWrapper.orderByDesc("u."+sort);
        }
        IPage<CourseParticipantsVO> usersPage = courseParticipantsService.getParticipantsPage(new Page<>(current, size),id , queryWrapper);
        return new GeneralDataResponse<>(usersPage);
    }

    @GetMapping("/list")
    @Operation(summary = "获取课程列表")
    public ResponseEntity<IPage<UserCoursesVO>> getUserCoursesPage(
            @RequestParam(defaultValue = "1") @Min(0) int pageNum,
            @RequestParam(defaultValue = "10") @Min(0) int pageSize) {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser")
        {
            throw new AccessDeniedException("You need to login first");
        }
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Integer userId = (Integer) users.getUserId();
        IPage<UserCoursesVO> result = courseParticipantsService.getUserCoursesPage(new Page<>(pageNum, pageSize), userId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add")
    @Operation(summary = "添加课程")
    @PreAuthorize("@AuthExpression.isTeacher()")
    public BaseResponse addCourse(
            @RequestBody CreateCourseDTO createCourseDTO
    ) {
        Courses course = new Courses();
        if (createCourseDTO.getStartTime() == null || createCourseDTO.getEndTime() == null) {
            throw new IllegalArgumentException("开始时间和结束时间不能为空");
        }
        // 如果开始时间在结束时间之后
        if (createCourseDTO.getStartTime().isAfter(createCourseDTO.getEndTime())) {
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
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        Users teacher = usersService.getById(course.getUserId());
        CourseInfoVO courseInfoVO = new CourseInfoVO();
        courseInfoVO.setCourse(course);
        courseInfoVO.setTeacher(teacher);
        return new GeneralDataResponse<>(courseInfoVO);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除课程")
    @PreAuthorize("@CourseExpression.canEditCourse(#id)")
    public BaseResponse deleteCourse(
            @Min(value = 0, message = "课程id必须大于0") @PathVariable Integer id) {
        Courses course = coursesService.getById(id);
        if (course == null) {
            throw new IllegalArgumentException("课程不存在");
        }
        coursesService.removeById(id);
        return BaseResponse.makeResponse(200, "删除成功");
    }

}
