package usst.spm.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import usst.spm.entity.UserInfo;
import usst.spm.entity.Users;
import usst.spm.vo.UsersVO;

import java.io.Serializable;
import java.util.List;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
public interface IUsersService extends IService<Users> {
    Users login(String username, String password);
    Users getUserCached(Serializable userId);
    List<String> getRolesByUserId(Serializable userId);
    Boolean hasRole(Serializable userId, String role);
    UserInfo getUserInfo(Serializable userId);
    void updateUserRoles(Integer userId, List<String> roles);
    void removeUserRoles(Integer userId);
    void updateUserExt(Integer userId, String key, String value);
    void removeUserExt(Integer userId, String key);
    void removeUserAllExt(Integer userId);
    boolean canAddUser(String username, String email);
    Users createUser(Users user);
    Users getUserByEmail(String email);
    Users changeUserEmail(Integer userId, String email);
    boolean emailExists(String email);
    boolean nicknameExists(String nickname);
    boolean updateUser(Users user);
    List<Users> getAllUsers();
    void removeUserById(Integer userId);
    IPage<UsersVO> selectUsersWithRoles(Page<Object> objectPage, QueryWrapper<Users> queryWrapper);
}
