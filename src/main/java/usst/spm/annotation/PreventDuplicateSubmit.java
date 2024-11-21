package usst.spm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     防止重复提交，保持接口幂等性
 * </p>
 * @author jyzxc
 * @since 2024-08-
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PreventDuplicateSubmit {
    long lockTime() default 1000; // 默认锁定时间100ms
}
