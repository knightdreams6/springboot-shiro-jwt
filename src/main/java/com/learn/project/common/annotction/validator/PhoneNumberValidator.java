package com.learn.project.common.annotction.validator;

import com.learn.project.common.annotction.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author lixiao
 * @date 2019/12/13 15:35
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

	@Override
	public boolean isValid(String phoneField, ConstraintValidatorContext context) {
		if (phoneField == null) {
			// 不允许为null
			return false;
		}
		return phoneField.matches("^1(3[0-9]|4[57]|5[0-35-9]|8[0-9]|70)\\d{8}$");
	}

}
