package com.learn.project.framework.manager.factory;

import com.learn.project.common.utils.MailUtils;
import com.learn.project.common.utils.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步任务生产工厂
 *
 * @author lixiao
 * @date 2020/6/21 21:17
 */
public class AsyncTaskFactory {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTaskFactory.class);

    public static TimerTask sendMailTask(String subject, String content, String... to) {
        return new TimerTask() {
            @Override
            public void run() {
                MailUtils mailUtils = SpringUtils.getBean("mailUtils");
                mailUtils.sendSimpleMailMessage(subject, content, to);
            }
        };
    }
}
