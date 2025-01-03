package usst.spm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import usst.spm.dto.CreateAnnouncementDTO;
import usst.spm.entity.Announcements;
import usst.spm.entity.UserLogin;
import usst.spm.result.BaseDataResponse;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.IAnnouncementsService;
import usst.spm.service.ICoursesService;
import usst.spm.service.INotificationsService;
import usst.spm.service.IUsersService;
import usst.spm.vo.AnnouncementsVO;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 公告表 前端控制器
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
@RestController
@RequestMapping("/course")
@Tag(name = "公告管理接口", description = "公告相关接口")
public class AnnouncementsController {
    @Resource
    IAnnouncementsService announcementsService;
    @Resource
    IUsersService usersService;
    @Resource
    ICoursesService coursesService;
    @Resource
    INotificationsService notificationsService;

    @GetMapping("/{id}/announcement")
    @Operation(summary = "获取课程公告", description = "获取课程公告")
    @Parameters({
            @Parameter(name = "id", description = "课程ID，最小为0"),
            @Parameter(name = "current", description = "当前页数，最小为1"),
            @Parameter(name = "size", description = "每页数量，范围在0~100")
    })
    @PreAuthorize("@CourseExpression.canAccessCourse(#id)")
    public GeneralDataResponse<IPage<Announcements>> getAnnouncementByCompetitionId(
            @PathVariable @Min(value = 0, message = "最小为0") Integer id,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "最小为1") int current,
            @RequestParam(defaultValue = "10") @Range(min = 0, max = 100, message = "范围控制在0~100之间") int size
    ) {
        if (size <= 0 || size > 100) {
            size = 10;
        }

        QueryWrapper<Announcements> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("course_id", id);
        queryWrapper.orderByDesc("created_at");
        IPage<Announcements> page = announcementsService.page(new Page<>(current, size), queryWrapper);
        return new GeneralDataResponse<>(page);
    }

    @GetMapping("/announcements/list")
    @Operation(summary = "获取公告列表", description = "获取用户参加的所有课程的所有公告")
    public BaseResponse getAnnouncements(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "最小为1") int current,
            @RequestParam(defaultValue = "10") @Range(min = 0, max = 100, message = "范围控制在0~100之间") int size
    ) {
        if (size <= 0 || size > 100) {
            size = 10;
        }
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        IPage<Announcements> page = announcementsService.getUserCourseAnnouncementsPage((Integer) users.getUserId(), current, size);
        List<AnnouncementsVO> announcementsVOList = new ArrayList<>();
        for (Announcements announcement : page.getRecords()) {
            AnnouncementsVO announcementsVO = new AnnouncementsVO();
            announcementsVO.setAnnouncement(announcement);
            announcementsVO.setCourse(coursesService.getById(announcement.getCourseId()));
            announcementsVO.setUser(usersService.getById(announcement.getCreatorId()));
            announcementsVOList.add(announcementsVO);
        }
        return new BaseDataResponse(announcementsVOList);
    }

    @PostMapping("/{courseId}/announcement")
    @Operation(summary = "添加公告", description = "添加公告")
    @PreAuthorize("@CourseExpression.canAccessCourse(#courseId)")
    public BaseResponse addAnnouncement(
            @PathVariable Integer courseId,
            @RequestBody CreateAnnouncementDTO createAnnouncementDTO
    ) {
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Announcements announcement = new Announcements();
        announcement.setCourseId(courseId);
        announcement.setCreatorId((Integer) users.getUserId());
        announcement.setTitle(createAnnouncementDTO.getTitle());
        announcement.setContent(createAnnouncementDTO.getContent());
        notificationsService.sendNotification((Integer) users.getUserId(), createAnnouncementDTO.getTitle(), createAnnouncementDTO.getContent());
        if (!announcementsService.save(announcement)) {
            return BaseResponse.makeResponse(400, "添加失败");
        }
        return BaseResponse.makeResponse(200, "添加成功");
    }
}
