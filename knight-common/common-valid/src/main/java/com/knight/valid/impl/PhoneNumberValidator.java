package com.knight.valid.impl;

import cn.hutool.core.util.StrUtil;
import com.knight.valid.annotation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author lixiao
 * @date 2019/12/13 15:35
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {

	@Override
	public boolean isValid(String phoneField, ConstraintValidatorContext context) {
		if (StrUtil.isBlank(phoneField)) {
			return false;
		}
		return phoneField.matches("^1[0-9]{10}$");
	}

}
