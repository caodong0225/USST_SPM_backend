package usst.spm.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import usst.spm.entity.UserRoles;
import usst.spm.mapper.UserRolesMapper;
import usst.spm.service.IUserRolesService;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Service
public class UserRolesServiceImpl extends ServiceImpl<UserRolesMapper, UserRoles> implements IUserRolesService {

    @Override
    public boolean isUserAdmin(Integer userId) {
        return this.lambdaQuery()
                .eq(UserRoles::getUserId, userId)
                .in(UserRoles::getRoleName, "admin", "super-admin")
                .count() > 0;
    }
}