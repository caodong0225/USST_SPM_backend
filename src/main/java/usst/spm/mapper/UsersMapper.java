package usst.spm.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import usst.spm.entity.Users;
import usst.spm.vo.UsersVO;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
public interface UsersMapper extends BaseMapper<Users> {
    @Select("SELECT u.*, ur.role_name FROM users u " +
            "LEFT JOIN user_roles ur ON u.id = ur.user_id " +
            "${ew.customSqlSegment}")
    IPage<UsersVO> selectUsersWithRoles(Page<?> page, @Param(Constants.WRAPPER) QueryWrapper<Users> queryWrapper);
}
