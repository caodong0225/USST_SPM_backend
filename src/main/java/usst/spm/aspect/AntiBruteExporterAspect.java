package usst.spm.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import usst.spm.annotation.AntiBrutePasswordExporter;
import usst.spm.vo.LoginDataResponseVO;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.RedisService;

import static usst.spm.util.HeaderUtil.getClientIp;

/**
 * <p>
 *     消息推送的事件检查切面
 * </p>
 * @author jyzxc
 */
@Aspect
@Component
@RequiredArgsConstructor
public class AntiBruteExporterAspect {
    private final RedisService redisService;
    @Around("@annotation(usst.spm.annotation.AntiBrutePasswordExporter)")
    public Object antiBrutePasswordExporter(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解信息
        AntiBrutePasswordExporter annotation = ((MethodSignature) joinPoint.getSignature())
                .getMethod()
                .getAnnotation(AntiBrutePasswordExporter.class);
        int retryTime = annotation.waitTime();
        int maxTry = annotation.maxTry();

        // 获取方法的传参
        Object[] args = joinPoint.getArgs();
        HttpSession session = (HttpSession) args[1];
        HttpServletRequest request = (HttpServletRequest) args[3];
        String loginIp = getClientIp(request);
        String key = "app:login:" + loginIp;

        // 在目标方法执行之前进行检查
        // 如果用户尚未登录
        if (session.getAttribute("userSession") == null) {
            if (redisService.hasKey(key)) {
                int times = Integer.parseInt(redisService.getValue(key).toString());
                if (times > maxTry) {
                    //TODO 疑似爆破，限制登录并告警用户，告诉管理员目标IP地址
                    return new GeneralDataResponse<>(400, "您已被限制登录，请过" + redisService.getKeyExpiry(key) + "秒后再试");
                }
            }
        }
        // 执行目标方法
        Object result = joinPoint.proceed();

        // 如果登录成功，清除失败次数
        // 如果用户尚未登录
        if (session.getAttribute("userSession") == null) {
            if (result instanceof LoginDataResponseVO) {
                redisService.setValue(key, "0", 1);
            } else {
                if (redisService.hasKey(key)) {
                    int times = Integer.parseInt(redisService.getValue(key).toString()) + 1;
                    redisService.setValue(key, Integer.toString(times), retryTime);
                } else {
                    redisService.setValue(key, "1", retryTime);
                }
            }
        }

        return result;
    }
}
