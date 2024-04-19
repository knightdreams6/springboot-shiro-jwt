package com.knight.valid.impl;

import cn.hutool.core.util.StrUtil;
import com.knight.valid.annotation.PhoneNumber;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author lixiao
 * @since 2019/12/13 15:35
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

	@Override
	public boolean isValid(String phoneField, ConstraintValidatorContext context) {
		if (StrUtil.isBlank(phoneField)) {
			return false;
		}
		return phoneField.matches("^1\\d{10}$");
	}

}
