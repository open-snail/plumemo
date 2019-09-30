package com.byteblogs.common.validator.annotion;

import com.byteblogs.common.validator.constraint.IdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;

/**
 *  这里有些冗余了，其实面对控制器的VO对象，应该全为String类型。
 *  作为后端程序员，不应该相信前端传递的任何参数，所以字符串类型也应该被识别。
 * @author Andy Chen
 * @date 9/30/19 1:24 PM
 */
@Target({TYPE, ANNOTATION_TYPE,FIELD,PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IdValidator.class})
public @interface NotNull {
    String message() default "";

    String value() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
