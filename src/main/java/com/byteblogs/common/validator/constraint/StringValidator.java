package com.byteblogs.common.validator.constraint;

import com.byteblogs.common.validator.annotion.NotBlank;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 不为空字符串
 * @author Andy Chen
 * @date 9/30/19 1:10 PM
 */
public class StringValidator implements ConstraintValidator<NotBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || StringUtils.isBlank(value)) {
            return false;
        }
        return true;
    }
}
