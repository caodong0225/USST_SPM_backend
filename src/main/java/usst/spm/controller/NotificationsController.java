package usst.spm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import usst.spm.entity.Notifications;
import usst.spm.entity.UserLogin;
import usst.spm.result.BaseResponse;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.INotificationsService;

import java.util.List;


/**
 * <p>
 * 通知表 前端控制器
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
@RestController
@Tag(name = "通知接口", description = "通知相关接口")
@RequestMapping("/notification")
public class NotificationsController {
    private final INotificationsService notificationsService;

    @Autowired
    public NotificationsController(
            INotificationsService notificationsService
    ) {
        this.notificationsService = notificationsService;
    }

    @GetMapping("/list")
    @Operation(summary = "获取通知列表")
    @Parameters({
            @Parameter(name = "unread", description = "是否未读，只能为true或者false，非必须")
    })
    public GeneralDataResponse<List<Notifications>> getUnreadNotifications(
            @RequestParam(required = false)
                    @Pattern(regexp = "true|false", message = "unread只能为true或者false")
            String unread
    ) {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Boolean unreadBoolean = null;
        if (unread != null) {
            if ("true".equals(unread)) {
                unreadBoolean = true;
            } else if ("false".equals(unread)) {
                unreadBoolean = false;
            }
        }

        return new GeneralDataResponse<>(
                notificationsService.lambdaQuery()
                        .eq(Notifications::getUserId, user.getUserId())
                        .eq(unreadBoolean != null, Notifications::getIsRead, unreadBoolean != null ? !unreadBoolean : null)
                        .orderBy(true, false, Notifications::getCreatedAt)
                        .last("limit 100")
                        .list()
        );
    }

    @GetMapping("/count")
    @Operation(summary = "获取未读通知数量")
    public GeneralDataResponse<Long> getUnreadNotificationsCount() {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return new GeneralDataResponse<>(
                notificationsService.lambdaQuery()
                        .eq(Notifications::getUserId, user.getUserId())
                        .eq(Notifications::getIsRead, false)
                        .count()
        );
    }

    @GetMapping("/read/{id}")
    @Operation(summary = "标记通知为已读")
    @Parameters({
            @Parameter(name = "id", description = "通知ID")
    })
    public BaseResponse readNotification(
            @PathVariable @Min(0) Integer id
    ) {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Notifications notification = notificationsService.getById(id);
        if (notification == null || !notification.getUserId().equals(user.getUserId())) {
            return new BaseResponse();
        }

        notification.setIsRead(true);
        notificationsService.updateById(notification);

        return new BaseResponse();
    }

    @GetMapping("/read/all")
    @Operation(summary = "标记所有通知为已读")
    public BaseResponse readAllNotifications() {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        notificationsService.lambdaUpdate()
                .eq(Notifications::getUserId, user.getUserId())
                .set(Notifications::getIsRead, true)
                .update();

        return new BaseResponse();
    }
}
