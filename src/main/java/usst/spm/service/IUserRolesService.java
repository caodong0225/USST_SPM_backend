package usst.spm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.UserRoles;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
public interface IUserRolesService extends IService<UserRoles> {
    boolean isUserAdmin(Integer userId);
}
