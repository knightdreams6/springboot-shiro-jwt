package com.knight.storage.response;

import com.knight.entity.enums.IResultConstants;
import lombok.Getter;

/**
 * 存储模块返回值常量
 *
 * @author knight
 * @since 2023/06/29
 */
@Getter
public enum StorageResultConstants implements IResultConstants {

	/**
	 * 未找到分片文件
	 */
	MULTIPART_EMPTY(1, "未找到分片文件"),

	/**
	 * 分片信息保存失败
	 */
	PART_INFO_SAVE_FAIL(2, "分片信息保存失败"),

	/**
	 * 分片上传开始失败
	 */
	MULTIPART_START_FAIL(3, "分片上传开始失败"),

	;

	private final Integer code;

	private final String msg;

	private String msgReally;

	private String enMsg;

	StorageResultConstants(Integer code, String msg) {
		this.code = 10000 + code;
		this.msg = msg;
	}

	StorageResultConstants(Integer code, String msg, String enMsg) {
		this(code, msg);
		this.enMsg = enMsg;
	}

	StorageResultConstants(Integer code, String msg, String msgReally, String enMsg) {
		this(code, msg, enMsg);
		this.msgReally = msgReally;
	}

	@Override
	public Integer code() {
		return getCode();
	}

	@Override
	public String msg() {
		return getMsg();
	}

	@Override
	public String msgReally() {
		return getMsgReally();
	}

	@Override
	public String enMsg() {
		return getEnMsg();
	}

}
