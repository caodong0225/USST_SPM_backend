package usst.spm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.Announcements;
import usst.spm.mapper.AnnouncementsMapper;
import usst.spm.service.IAnnouncementsService;

import java.util.List;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
@Service
public class AnnouncementsServiceImpl extends ServiceImpl<AnnouncementsMapper, Announcements> implements IAnnouncementsService {
    @Override
    public List<Announcements> getLatestAnnouncements(int count) {
        QueryWrapper<Announcements> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        queryWrapper.last("LIMIT " + count);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public IPage<Announcements> getUserCourseAnnouncementsPage(Integer userId, int current, int size) {
        return this.baseMapper.getUserCourseAnnouncementsPage(new Page<>(current, size), userId);
    }
}