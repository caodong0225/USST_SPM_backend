package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.Notifications;
import usst.spm.mapper.NotificationsMapper;
import usst.spm.service.INotificationsService;

/**
 * <p>
 * 通知表 服务实现类
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
@Service
public class NotificationsServiceImpl extends ServiceImpl<NotificationsMapper, Notifications> implements INotificationsService {
    @Override
    public Notifications sendNotification(Integer toUser, String title, String content) {
        Notifications notifications = new Notifications();
        notifications.setUserId(toUser);
        notifications.setTitle(title);
        notifications.setContent(content);
        baseMapper.insert(notifications);
        return notifications;
    }
}
