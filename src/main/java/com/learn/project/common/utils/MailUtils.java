package com.learn.project.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author li'xiao
 * @version 1.0
 * @date 2020-03-20 10:18
 */
@Slf4j
@Component
public class MailUtils {

    @Resource
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sender;

    /**
     * 发送普通邮件
     *
     * @param to      收件人
     * @param subject 主题
     * @param content 内容
     */
    public void sendSimpleMailMessage(String subject, String content, String... to) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        try {
            mailSender.send(message);
            log.info("发送简单邮件到{}成功", Arrays.toString(to));
        } catch (Exception e) {
            log.error("发送简单邮件时发生异常!", e);
        }
    }
}