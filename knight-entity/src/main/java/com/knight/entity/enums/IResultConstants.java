package com.knight.entity.enums;

/**
 * 结果常量接口
 *
 * @author knight
 * @since 2022/10/07
 */
public interface IResultConstants {

	Integer code();

	String msg();

	default String msgReally() {
		return msg();
	}

	default String enMsg() {
		return msg();
	}

}
