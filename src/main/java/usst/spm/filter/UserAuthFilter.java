package usst.spm.filter;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import usst.spm.entity.UserLogin;
import usst.spm.entity.Users;
import usst.spm.result.UserSession;
import usst.spm.service.IUsersService;

import java.io.IOException;

/**
 * @author jyzxc
 * @since 2024-11-12
 */
@Component
public class UserAuthFilter implements Filter {
    private final IUsersService usersService;
    @Autowired
    public UserAuthFilter(IUsersService usersService) {
        this.usersService = usersService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setAttribute("startTime", System.currentTimeMillis());
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        HttpSession session = httpRequest.getSession(false);
        if(session == null) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        if (session.getAttribute("userSession") == null) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }
        UserSession userSession = JSON.parseObject(JSON.toJSONString(session.getAttribute("userSession")), UserSession.class);
        Integer userId = userSession.getUserId();
        Users user = usersService.getUserCached(userId);
        if (user == null) {
            chain.doFilter(httpRequest, httpResponse);
            return;
        }

        UserDetails userDetails = new UserLogin(user.getUsername(), usersService.getRolesByUserId(user.getId()), user.getId(),userSession.getSessionId());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
        authToken.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(httpRequest)
        );
        SecurityContextHolder.getContext().setAuthentication(authToken);

        chain.doFilter(httpRequest, httpResponse);
    }

}
