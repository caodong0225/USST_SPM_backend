package usst.spm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 *     防止用户暴力破解密码的注解
 * </p>
 * @author jyzxc
 * @since 2024-08-18
 */
@Target({ElementType.METHOD})
//注解在运行时有效
@Retention(RetentionPolicy.RUNTIME)
public @interface AntiBrutePasswordExporter {
    //定义最大尝试次数
    int maxTry() default 5;
    //定义超过最大次数的等待时间
    int waitTime() default 60;
}
