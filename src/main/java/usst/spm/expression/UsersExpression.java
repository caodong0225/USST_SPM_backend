package usst.spm.expression;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import usst.spm.entity.UserLogin;
import usst.spm.entity.Users;
import usst.spm.service.IUsersService;

/**
 * @author jyzxc
 * @since 2024-11-26
 */
@Component("UsersExpression")
@RequiredArgsConstructor
public class UsersExpression {
    private final IUsersService usersService;
    public boolean isUserExist(Integer userId) {
        if(userId==null)
        {
            throw new IllegalArgumentException("Id不能为空");
        }
        if(userId<0)
        {
            throw new IllegalArgumentException("Id必须大于等于0");
        }
        Users user = usersService.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户未找到");
        }
        return true;
    }
    public boolean isUserSame(Integer userId) {
        UserLogin user = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getUserId().equals(userId)) {
            throw new IllegalArgumentException("你不能修改自己的信息");
        }
        return true;
    }
}
