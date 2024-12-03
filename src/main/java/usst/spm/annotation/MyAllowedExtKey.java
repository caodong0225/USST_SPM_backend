package usst.spm.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import usst.spm.validation.MyAllowedExtKeyValidation;

import java.lang.annotation.*;

/**
 * @author jyzxc
 */
@Documented
@Constraint(validatedBy = MyAllowedExtKeyValidation.class)
@Target({ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER,ElementType.ANNOTATION_TYPE,ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAllowedExtKey {
    String message() default "非法的键值，键值只能为realName、schoolNumber、college、sex、description、pic";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
