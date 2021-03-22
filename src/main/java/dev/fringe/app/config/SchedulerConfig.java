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

import dev.fringe.app.service.SchedulerService;


@Configuration
public class SchedulerConfig implements InitializingBean{

	@Value("${thread.pool.size:20}") int THREAD_POOL_SIZE;
	@Value("${schedule.cron:1 * * * * *}") String CRON_EXPRESSION;
	//*/5 * * * *
	@Autowired SchedulerService testService;
	@Autowired TaskScheduler scheduler;
	
	@Bean
	public ThreadPoolTaskScheduler schedulerExecutor() {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(THREAD_POOL_SIZE);
		taskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		return taskScheduler;
	}

	/**
	 * 배치는 4개
	 * 일별 가져오는 거
	 * tick 가져오는 거
	 * 분석 하는거.
	 * 거래 및 매도 하는 거. 
	 * 
	 */
	public void afterPropertiesSet() throws Exception {
		ScheduledFuture<?> schdule1 = (ScheduledFuture<?>) this.scheduler.schedule(() -> {testService.tick();}, new CronTrigger(CRON_EXPRESSION)); 
//		schdule1.isDone();
//		ScheduledFuture<?> schdule2 = (ScheduledFuture<?>) this.scheduler.schedule(() -> {testService.test();}, new CronTrigger(CRON_EXPRESSION));
//		schdule2.isDone();
	}

}