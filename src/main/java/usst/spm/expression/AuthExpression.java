package usst.spm.expression;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import usst.spm.entity.UserLogin;


/**
 * @author jyzxc
 * @since 2024-11-26
 */
@Component("AuthExpression")
public class AuthExpression {
    public boolean isTeacher() {
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == "anonymousUser")
        {
            throw new AccessDeniedException("You need to login first");
        }
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!users.isAdmin())
        {
            throw new AccessDeniedException("No permission to access");
        }
        else
        {
            return true;
        }
    }
    public boolean isSuperAdmin()
    {
        UserLogin users = (UserLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!users.getPermissions().contains("super-admin"))
        {
            throw new AccessDeniedException("No permission to access");
        }
        return true;
    }
}
