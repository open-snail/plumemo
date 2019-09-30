package com.byteblogs.common.validator.annotion;

import com.byteblogs.common.validator.Messages;
import com.byteblogs.common.validator.constraint.StringValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * 不为空字符串
 * @author Andy Chen
 * @date 9/30/19 1:10 PM
 */
@Target({TYPE, ANNOTATION_TYPE,FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {StringValidator.class})
public @interface NotBlank {
    String message() default Messages.CK_NOT_BLANK_DEFAULT;

    String value() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
