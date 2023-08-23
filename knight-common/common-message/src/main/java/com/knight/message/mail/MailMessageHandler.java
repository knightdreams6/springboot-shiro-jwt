package com.knight.message.mail;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.knight.message.Message;
import com.knight.message.MessageHandler;
import com.knight.message.MessagingException;
import com.knight.message.enums.MessageContentTypeEnums;
import com.knight.storage.template.StorageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Objects;

/**
 * 邮件消息处理
 *
 * @author knight
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MailMessageHandler implements MessageHandler {

	/**
	 * java邮件发送者
	 */
	private final JavaMailSender mailSender;

	/**
	 * 邮件属性
	 */
	private final MailProperties mailProperties;

	/**
	 * 存储模板
	 */
	private final StorageTemplate storageTemplate;

	@Override
	public void handleMessage(Message<?> message) throws MessagingException {
		Object payload = message.getPayload();
		if (!(payload instanceof MailMessage)) {
			return;
		}
		MailMessage mailMessage = (MailMessage) payload;
		// 如果发送人为空则使用
		if (StrUtil.isEmpty(mailMessage.getSender())) {
			mailMessage.setSender(mailProperties.getUsername());
		}

		if (CollUtil.isEmpty(mailMessage.getAttachmentList())
				&& Objects.equals(mailMessage.getContentType(), MessageContentTypeEnums.TEXT)) {
			sendSimpleMailMessage(mailMessage);
		}
		else {
			sendMimeMessage(mailMessage);
		}
	}

	/**
	 * 发送简单的邮件消息
	 * @param mailMessage 邮件消息
	 */
	public void sendSimpleMailMessage(MailMessage mailMessage) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(mailMessage.getSender());
		message.setSubject(mailMessage.getSubject());
		message.setText(mailMessage.getContent());
		if (CollUtil.isNotEmpty(mailMessage.getReceiver())) {
			message.setTo(mailMessage.getReceiver().toArray(new String[0]));
		}
		if (CollUtil.isNotEmpty(mailMessage.getCcList())) {
			message.setCc(mailMessage.getCcList().toArray(new String[0]));
		}
		if (CollUtil.isNotEmpty(mailMessage.getBccList())) {
			message.setBcc(mailMessage.getBccList().toArray(new String[0]));
		}
		try {
			mailSender.send(message);
		}
		catch (MailException e) {
			log.error("发送简单邮件时发生异常", e);
			throw new MessagingException(e.getLocalizedMessage());
		}
	}

	/**
	 * 发送mime消息
	 * @param mailMessage 邮件消息
	 */
	public void sendMimeMessage(MailMessage mailMessage) {
		MimeMessage message = mailSender.createMimeMessage();
		try {
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(mailMessage.getSender());
			helper.setSubject(mailMessage.getSubject());
			helper.setText(mailMessage.getContent(), true);
			if (CollUtil.isNotEmpty(mailMessage.getReceiver())) {
				helper.setTo(mailMessage.getReceiver().toArray(new String[0]));
			}
			if (CollUtil.isNotEmpty(mailMessage.getCcList())) {
				helper.setCc(mailMessage.getCcList().toArray(new String[0]));
			}
			if (CollUtil.isNotEmpty(mailMessage.getBccList())) {
				helper.setBcc(mailMessage.getBccList().toArray(new String[0]));
			}
			// 如果附件不为空
			if (CollUtil.isNotEmpty(mailMessage.getAttachmentList())) {
				// 下载文件
				for (MailAttachmentVo attachmentVo : mailMessage.getAttachmentList()) {
					// 下载目录
					String filePath = storageTemplate.filePathGenerate(attachmentVo.getFileName());
					storageTemplate.download(attachmentVo.getCloudPath(), filePath);
					// 添加附件
					helper.addAttachment(attachmentVo.getFileName(), new File(filePath));
				}
			}
			mailSender.send(message);
		}
		catch (Exception e) {
			log.error("发送Mime邮件时发生异常！", e);
		}
	}

}
