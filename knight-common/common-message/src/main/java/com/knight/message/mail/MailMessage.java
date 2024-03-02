package com.knight.message.mail;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.knight.message.enums.MessageContentTypeEnums;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 邮件消息
 *
 * @author knight
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MailMessage {

	/**
	 * 发送方
	 */
	@Builder.Default
	private String sender = StringPool.EMPTY;

	/**
	 * 接收者
	 */
	private Set<String> receiver;

	/**
	 * 消息内容
	 */
	private String content;

	/**
	 * 主题
	 */
	private String subject;

	/**
	 * 内容类型
	 */
	@Builder.Default
	private MessageContentTypeEnums contentType = MessageContentTypeEnums.TEXT;

	/**
	 * 抄送人列表
	 */
	@Builder.Default
	private List<String> ccList = Collections.emptyList();

	/**
	 * 秘密抄送人列表
	 */
	@Builder.Default
	private List<String> bccList = Collections.emptyList();

	/**
	 * 附件列表
	 */
	@Builder.Default
	private List<MailAttachmentVo> attachmentList = Collections.emptyList();

}
