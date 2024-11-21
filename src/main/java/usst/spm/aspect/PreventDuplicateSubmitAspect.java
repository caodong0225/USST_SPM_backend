package usst.spm.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import usst.spm.annotation.PreventDuplicateSubmit;
import usst.spm.controller.RegisterRequestDTO;
import usst.spm.result.GeneralDataResponse;
import usst.spm.service.RedisService;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 防止接口抖动问题
 * </p>
 *
 * @author jyzxc
 * @since 2024-08-25
 */
@Aspect
@Component
public class PreventDuplicateSubmitAspect {

    private final RedisService redisService;

    public PreventDuplicateSubmitAspect(RedisService redisService) {
        this.redisService = redisService;
    }

    @Around("@annotation(preventDuplicateSubmit)")
    public Object around(ProceedingJoinPoint joinPoint, PreventDuplicateSubmit preventDuplicateSubmit) throws Throwable {
        String key = generateKey(joinPoint);

        // 使用setIfAbsent加锁
        boolean isLocked = redisService.setIfAbsent(key, "locked", preventDuplicateSubmit.lockTime(), TimeUnit.MILLISECONDS);
        if (isLocked) {
            try {
                // 执行目标方法
                return joinPoint.proceed();
            } finally {
                // 可选：可以不手动解锁，依赖于锁的过期时间
                // redisService.removeValue(key); // 如果想在执行后立即解锁，可以解除注释
            }
        } else {
            // 如果已经锁定，直接返回一个重复提交的提示
            return new GeneralDataResponse<>(400, "请勿重复提交");
        }
    }

    // 生成唯一的锁Key，可以基于方法名和参数来生成
    private String generateKey(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0) {
            throw new IllegalArgumentException("参数错误");
        }
        RegisterRequestDTO registerRequestDTO = (RegisterRequestDTO) args[0];
        return registerRequestDTO.getUsername() + ":" + registerRequestDTO.getPassword();
    }
}
