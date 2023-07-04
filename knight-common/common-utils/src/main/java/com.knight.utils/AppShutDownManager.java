package com.knight.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * 确保应用退出时能关闭后台线程
 *
 * @author lixiao
 * @since 2020/6/21 21:05
 */
@Component
public class AppShutDownManager {

	private static final Logger logger = LoggerFactory.getLogger(AppShutDownManager.class);

	@PreDestroy
	public void destroy() {
		shutdownAsyncManager();
	}

	/**
	 * 停止异步执行任务
	 */
	private void shutdownAsyncManager() {
		try {
			logger.info("====关闭后台任务任务线程池====");
			AsyncManager.me().shutdown();
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

}
