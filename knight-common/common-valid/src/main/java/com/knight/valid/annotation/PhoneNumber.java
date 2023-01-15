package com.knight.valid.annotation;

import com.knight.valid.impl.PhoneNumberValidator;

import javax.validation.Constraint;
import java.lang.annotation.*;

/**
 * @author knight
 */
@Documented
@Constraint(validatedBy = PhoneNumberValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {

	String message() default "手机号无效";

	Class[] groups() default {};

	Class[] payload() default {};

}
