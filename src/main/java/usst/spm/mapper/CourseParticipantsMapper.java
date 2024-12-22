package usst.spm.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
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
    @Results({
            @Result(property = "courseParticipants.id", column = "courseParticipants_id"),
            @Result(property = "courseParticipants.courseId", column = "courseParticipants_courseId"),
            @Result(property = "courseParticipants.userId", column = "courseParticipants_userId"),
            @Result(property = "courseParticipants.status", column = "courseParticipants_status"),
            @Result(property = "courseParticipants.comment", column = "courseParticipants_comment"),
            @Result(property = "users.id", column = "users_id"),
            @Result(property = "users.username", column = "users_username"),
            @Result(property = "users.nickname", column = "users_nickname"),
            @Result(property = "users.email", column = "users_email"),
            @Result(property = "users.phone", column = "users_phone")
    })
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
