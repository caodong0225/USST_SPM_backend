package usst.spm.exception;

import com.alibaba.fastjson.JSON;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * @author jyzxc
 * @since 2024-07-29
 */
@ControllerAdvice
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint{
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return sdf.format(new Date());
    }
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        HashMap<String, Object> result = new HashMap<>();
        // 设置HTTP状态码为403
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        // 设置响应体
        result.put("code", 403);
        result.put("message", "permission denied");
        String json = JSON.toJSONString(result);
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

}