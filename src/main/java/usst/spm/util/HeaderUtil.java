package usst.spm.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author jyzxc
 * @since 2024-11-21
 */
public class HeaderUtil {

    /**
     * 获取客户端IP地址
     *
     * @param request 请求
     * @return IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-Forwarded-For");
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getHeader("Proxy-Client-IP");
            }
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getHeader("WL-Proxy-Client-IP");
            }
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getHeader("HTTP_CLIENT_IP");
            }
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getHeader("X-Real-IP");
            }
            if (remoteAddr == null || remoteAddr.isEmpty()) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }
}