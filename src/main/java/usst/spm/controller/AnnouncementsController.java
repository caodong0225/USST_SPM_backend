package usst.spm.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import usst.spm.entity.Announcements;
import usst.spm.result.BaseDataResponse;
import usst.spm.result.BaseResponse;
import usst.spm.service.IAnnouncementsService;


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
    private final IAnnouncementsService announcementsService;


    @Autowired
    public AnnouncementsController(
            IAnnouncementsService announcementsService
    ) {
        this.announcementsService = announcementsService;
    }

    @GetMapping("/{id}/announcement")
    @Operation(summary = "获取课程公告", description = "获取课程公告")
    @Parameters({
            @Parameter(name = "id", description = "比赛ID，最小为0"),
            @Parameter(name = "current", description = "当前页数，最小为1"),
            @Parameter(name = "size", description = "每页数量，范围在0~100")
    })
    // TODO: 权限认证，确保只有教师可以查看公告
    public BaseResponse getAnnouncementByCompetitionId(
            @PathVariable @Min(value = 0, message = "最小为0") Integer id,
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "最小为1") int current,
            @RequestParam(defaultValue = "10") @Range(min = 0, max = 100, message = "范围控制在0~100之间") int size
    ) {
        if (size <= 0 || size > 100) {
            size = 10;
        }

        QueryWrapper<Announcements> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("competition_id", id);
        queryWrapper.orderByDesc("created_at");
        IPage<Announcements> page = announcementsService.page(new Page<>(current, size), queryWrapper);
        return new BaseDataResponse(page);
    }


}
