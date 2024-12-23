package usst.spm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import usst.spm.entity.UserExts;
import usst.spm.entity.UserInfo;
import usst.spm.entity.UserRoles;
import usst.spm.entity.Users;
import usst.spm.mapper.UsersMapper;
import usst.spm.service.IUserExtsService;
import usst.spm.service.IUserRolesService;
import usst.spm.service.IUsersService;
import usst.spm.service.RedisService;
import usst.spm.vo.UsersVO;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements IUsersService {

    private IUserRolesService userRolesService;
    private IUserExtsService userExtsService;
    private RedisService redisService;

    @Autowired
    public void setUserRolesService(IUserRolesService userRolesService, IUserExtsService userExtsService, RedisService redisService) {
        this.userRolesService = userRolesService;
        this.userExtsService = userExtsService;
        this.redisService = redisService;
    }

    @Override
    public Users login(String usernameOrEmail, String password) {
        Users user = this.lambdaQuery()
                .eq(Users::getUsername, usernameOrEmail)
                .or()
                .eq(Users::getEmail, usernameOrEmail)
                .one();
        if (user == null) {
            return null;
        }

        String realPassword = user.getHash();
        boolean isPasswordMatch = BCrypt.checkpw(password, realPassword);

        user.setHash(null);

        return isPasswordMatch ? user : null;
    }

    @Override
    @Cacheable(cacheNames = "app:session:user-info#3600", key = "#userId", unless = "#result == null")
    public Users getUserCached(Serializable userId) {
        return this.getById(userId);
    }

    @Override
    @Cacheable(cacheNames = "app:users:roles#3600", key = "#userId")
    public List<String> getRolesByUserId(Serializable userId) {
        return userRolesService.lambdaQuery()
                .eq(UserRoles::getUserId, userId)
                .list()
                .stream()
                .map(UserRoles::getRoleName)
                .toList();
    }

    @Override
    public Boolean hasRole(Serializable userId, String role) {
        List<String> roles = getRolesByUserId(userId);
        return roles.contains(role);
    }

    @Override
    @Cacheable(cacheNames = "app:users:user-info#600", key = "#userId", unless = "#result == null")
    public UserInfo getUserInfo(Serializable userId) {
        Users user = this.getById(userId);
        if (user == null) {
            return null;
        }

        user.setHash(null);

        UserInfo userInfo = new UserInfo();
        userInfo.setUser(user);
        userInfo.setUserRoles(userRolesService.lambdaQuery()
                .eq(UserRoles::getUserId, userId)
                .list()
                .stream()
                .map(UserRoles::getRoleName)
                .toList()
        );
        userInfo.setUserExtraInfo(userExtsService.lambdaQuery()
                .eq(UserExts::getUserId, userId)
                .list()
                .stream()
                .collect(Collectors.toMap(UserExts::getKey, UserExts::getValue)));

        return userInfo;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "app:users:roles", key = "#userId"),
            @CacheEvict(cacheNames = "app:users:user-info#600", key = "#userId")
    })
    public void updateUserRoles(Integer userId, List<String> roles) {
        userRolesService.lambdaUpdate()
                .eq(UserRoles::getUserId, userId)
                .remove();
        roles.forEach(role -> {
            UserRoles userRoles = new UserRoles();
            userRoles.setUserId(userId);
            userRoles.setRoleName(role);
            userRolesService.save(userRoles);
        });
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "app:users:roles", key = "#userId"),
            @CacheEvict(cacheNames = "app:users:user-info#600", key = "#userId")
    })
    public void removeUserRoles(Integer userId) {
        userRolesService.lambdaUpdate()
                .eq(UserRoles::getUserId, userId)
                .remove();
    }

    @Override
    @CacheEvict(cacheNames = "app:users:user-info#600", key = "#userId")
    public void updateUserExt(Integer userId, String key, String value) {
        UserExts userExts = userExtsService.lambdaQuery()
                .eq(UserExts::getUserId, userId)
                .eq(UserExts::getKey, key)
                .one();
        if (userExts == null) {
            userExts = new UserExts();
            userExts.setUserId(userId);
            userExts.setKey(key);
        }
        userExts.setValue(value);
        userExtsService.saveOrUpdate(userExts);
    }

    @Override
    @CacheEvict(cacheNames = "app:users:user-info#600", key = "#userId")
    public void removeUserExt(Integer userId, String key) {
        userExtsService.lambdaUpdate()
                .eq(UserExts::getUserId, userId)
                .eq(UserExts::getKey, key)
                .remove();
    }

    @Override
    @CacheEvict(cacheNames = "app:users:user-info#600", key = "#userId")
    public void removeUserAllExt(Integer userId) {
        userExtsService.lambdaUpdate()
                .eq(UserExts::getUserId, userId)
                .remove();
    }

    @Override
    public boolean canAddUser(String username, String email) {
        return this.lambdaQuery()
                .eq(Users::getUsername, username)
                .or()
                .eq(Users::getEmail, email)
                .count() != 0;
    }

    @Override
    public Users createUser(Users user) {
        user.setHash(BCrypt.hashpw(user.getHash(), BCrypt.gensalt()));
        this.save(user);
        redisService.addToBloomFilter("users:bloom", user.getId().toString());
        return user;
    }

    @Override
    public Users getUserByEmail(String email) {
        return this.lambdaQuery()
                .eq(Users::getEmail, email)
                .one();
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "app:users:user-info#600", key = "#userId"),
            @CacheEvict(cacheNames = "app:session:user-info", key = "#userId")
    })
    public Users changeUserEmail(Integer userId, String email) {
        return this.lambdaUpdate()
                .eq(Users::getId, userId)
                .set(Users::getEmail, email)
                .update()
                ? this.getById(userId)
                : null;
    }

    @Override
    public boolean emailExists(String email) {
        return this.lambdaQuery()
                .eq(Users::getEmail, email)
                .count() > 0;
    }

    @Override
    public boolean nicknameExists(String nickname) {
        return this.lambdaQuery()
                .eq(Users::getNickname, nickname)
                .count() > 0;
    }

    @Override
    @Caching(evict = {
            @CacheEvict(cacheNames = "app:users:user-info#600", key = "#user.id"),
            @CacheEvict(cacheNames = "app:session:user-info", key = "#user.id")
    })
    public boolean updateUser(Users user) {
        return this.updateById(user);
    }

    @Override
    public List<Users> getAllUsers() {
        return this.list();
    }

    @Override
    @CacheEvict(cacheNames = "app:session:user-info", key = "#userId")
    public void removeUserById(Integer userId) {
        this.removeById(userId);
    }

    @Override
    public IPage<UsersVO> selectUsersWithRoles(Page<Object> objectPage, QueryWrapper<Users> queryWrapper) {
        return this.baseMapper.selectUsersWithRoles(objectPage, queryWrapper);
    }

}
