package com.knight.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author lixiao
 * @since 2020/6/21 19:54 线程池相关配置
 */
@Configuration
public class ThreadPoolConfig {

	/**
	 * 核心线程数：默认值为1 参数描述：当线程池小于corePoolSize时，新提交任务将创建一个新线程执行任务，即使此时线程池中存在空闲线程
	 */
	private final int corePoolSize = 5;

	/**
	 * 最大可创建的线程数：默认值为Integer.MAX_VALUE 参数描述：线程池中允许的最大线程数，线程池中的当前线程数目不会超过该值。
	 * 如果队列中任务已满，并且当前线程个数小于maximumPoolSize，那么会创建新的线程来执行任务
	 */
	private final int maxPoolSize = 200;

	/**
	 * 队列最大长度：默认值为Integer.MAX_VALUE 参数描述：存储任务的队列长度
	 */
	private final int queueCapacity = 1000;

	/**
	 * 线程池维护线程所允许的空闲时间：默认值为60s 参数描述：当线程池中超过corePoolSize线程，空闲时间达到keepAliveTime时，关闭空闲线程
	 */
	private final int keepAliveSeconds = 300;

	/**
	 * 默认值：false 参数描述：设置为true时，线程池中corePoolSize线程空闲时间达到keepAliveTime也将关闭，通常是不必要的
	 */
	private final boolean setAllowCoreThreadTimeOut = false;

	/**
	 * 参数描述：当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略 四种拒绝策略：
	 * 1.AbortPolicy 丢弃任务并抛出RejectedExecutionException异常。 2.DiscardPolicy
	 * 丢弃任务，但是不抛出异常。如果线程队列已满，则后续提交的任务都会被丢弃，且是静默丢弃。 3.DiscardOldestPolicy
	 * 丢弃队列最前面的任务，然后重新提交被拒绝的任务。 4.CallerRunsPolicy 由调用线程处理该任务
	 */
	private final RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();

	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
		executor.setMaxPoolSize(maxPoolSize);
		executor.setQueueCapacity(queueCapacity);
		executor.setKeepAliveSeconds(keepAliveSeconds);
		executor.setRejectedExecutionHandler(rejectedExecutionHandler);
		executor.setAllowCoreThreadTimeOut(setAllowCoreThreadTimeOut);
		return executor;
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		// 核心线程数
		int corePoolSize = Runtime.getRuntime().availableProcessors();
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		// 池数量
		threadPoolTaskScheduler.setPoolSize(corePoolSize);
		// 拒绝策略
		threadPoolTaskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		return threadPoolTaskScheduler;
	}

}
