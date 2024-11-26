package usst.spm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.Notifications;

/**
 * <p>
 * 通知表 服务类
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
public interface INotificationsService extends IService<Notifications> {

    Notifications sendNotification(Integer toUser, String title, String content);
}
