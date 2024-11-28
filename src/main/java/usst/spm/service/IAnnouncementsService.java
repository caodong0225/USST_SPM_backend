package usst.spm.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.Announcements;

import java.util.List;

/**
 * <p>
 * 公告表 服务类
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
public interface IAnnouncementsService extends IService<Announcements> {
    List<Announcements> getLatestAnnouncements(int count);
    IPage<Announcements> getUserCourseAnnouncementsPage(Integer userId, int current, int size);
}
