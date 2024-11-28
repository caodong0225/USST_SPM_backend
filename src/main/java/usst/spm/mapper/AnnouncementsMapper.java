package usst.spm.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import usst.spm.entity.Announcements;

/**
 * <p>
 * 公告表 Mapper 接口
 * </p>
 *
 * @author vvbbnn00
 * @since 2024-06-06
 */
public interface AnnouncementsMapper extends BaseMapper<Announcements> {

    @Select("""
    SELECT 
        a.id AS announcementId,
        a.title AS title,
        a.content AS content,
        a.created_at AS createdAt,
        a.updated_at AS updatedAt,
        a.course_id AS courseId,
        a.creator_id AS creatorId
    FROM 
        announcements a
    JOIN 
        course_participants cp ON a.course_id = cp.course_id
    WHERE 
        cp.user_id = #{userId}
    ORDER BY 
        a.created_at DESC
""")
    IPage<Announcements> getUserCourseAnnouncementsPage(Page<?> page, @Param("userId") Integer userId);
}
