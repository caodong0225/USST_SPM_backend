package usst.spm.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import usst.spm.annotation.MyAllowedExtKey;

/**
 * <p>
 *     校验用户接口的相关的数据
 * </p>
 * @author jyzxc
 * @since 2024-08-09
 */
public class MyAllowedExtKeyValidation implements ConstraintValidator<MyAllowedExtKey, String> {
    private static final String[] ALLOWED_EXT_KEYS = new String[]{
            "realName",
            "schoolNumber",
            "college",
            "sex",
            "description",
    };
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value!=null){
            for (String allowedKey : ALLOWED_EXT_KEYS) {
                if (allowedKey.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
}
