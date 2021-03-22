package dev.fringe.app.config;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import dev.fringe.app.service.MarketService;


@Configuration
public class SchedulerConfig implements InitializingBean{

	@Value("${thread.pool.size:20}") int THREAD_POOL_SIZE;
	@Value("${schedule.cron:* * * * * *}") String CRON_EXPRESSION;
	@Autowired MarketService testService;
	@Autowired TaskScheduler scheduler;
	
	@Bean
	public ThreadPoolTaskScheduler schedulerExecutor() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(THREAD_POOL_SIZE);
		taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		return taskScheduler;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ScheduledFuture<?> schdule1 = (ScheduledFuture<?>) this.scheduler.schedule(() -> {
			testService.test();
		}, new CronTrigger(CRON_EXPRESSION));
		schdule1.isDone();
		ScheduledFuture<?> schdule2 = (ScheduledFuture<?>) this.scheduler.schedule(() -> {
			testService.test();
		}, new CronTrigger(CRON_EXPRESSION));
		schdule2.isDone();
	}

}