package com.knight.message.mail;

import lombok.Data;

import java.io.Serializable;

/**
 * 电子邮件附件vo
 *
 * @author knight
 */
@Data
public class MailAttachmentVo implements Serializable {

	/**
	 * 云端路径
	 */
	private String cloudPath;

	/**
	 * 文件名称
	 */
	private String fileName;

}
