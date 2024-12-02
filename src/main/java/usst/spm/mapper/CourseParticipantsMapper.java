package usst.spm.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import usst.spm.entity.CourseParticipants;
import usst.spm.entity.Users;
import usst.spm.vo.CourseParticipantsVO;
import usst.spm.vo.UserCoursesVO;

/**
 * @author jyzxc
 * @since 2024-11-27
 */
public interface CourseParticipantsMapper extends BaseMapper<CourseParticipants> {
    @Select("""
    SELECT 
        cp.id AS courseParticipants_id,
        cp.course_id AS courseParticipants_courseId,
        cp.user_id AS courseParticipants_userId,
        cp.status AS courseParticipants_status,
        cp.comment AS courseParticipants_comment,
        u.id AS users_id,
        u.username AS users_username,
        u.nickname AS users_nickname,
        u.email AS users_email,
        u.phone AS users_phone
    FROM 
        course_participants cp
    LEFT JOIN 
        users u 
    ON 
        cp.user_id = u.id
    WHERE 
        cp.course_id = #{courseId}
        ${ew.customSqlSegment}
""")
    IPage<CourseParticipantsVO> getParticipantsPage(Page<?> page, Integer courseId, @Param(Constants.WRAPPER) QueryWrapper<Users> queryWrapper);

    @Select("""
    SELECT 
        c.id AS courseId,
        c.course_name AS courseName,
        c.course_desc AS courseDesc,
        c.course_pic AS coursePic,
        c.start_time AS startTime,
        c.end_time AS endTime,
        c.status AS status,
        cp.comment AS participantComment
    FROM 
        course_participants cp
    JOIN 
        courses c ON cp.course_id = c.id
    WHERE 
        cp.user_id = #{userId}
""")
    IPage<UserCoursesVO> getUserCoursesPage(Page<?> page, @Param("userId") Integer userId);
}
