package com.laurensius.springbootmultids.config;

import com.laurensius.springbootmultids.async.ExceptionHandlingAsyncTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfiguration implements AsyncConfigurer, SchedulingConfigurer {

    private final Logger log = LoggerFactory.getLogger(AsyncConfiguration.class);

    @Value("${async.core-pool-size}")
    private Integer PROPERTY_ASYNC_CORE_POOL_SIZE;

    @Value("${async.max-pool-size}")
    private Integer PROPERTY_ASYNC_MAX_POOL_SIZE;

    @Value("${async.queue-capacity}")
    private Integer PROPERTY_ASYNC_QUEUE_CAPACITY;


    @Override
    @Bean(name = "taskExecutor")
    public Executor getAsyncExecutor() {
        log.debug("Creating Async Task Executor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(PROPERTY_ASYNC_CORE_POOL_SIZE);
        executor.setMaxPoolSize(PROPERTY_ASYNC_MAX_POOL_SIZE);
        executor.setQueueCapacity(PROPERTY_ASYNC_QUEUE_CAPACITY);
        executor.setThreadNamePrefix("datasync-Executor-");
        return new ExceptionHandlingAsyncTaskExecutor(executor);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(scheduledTaskExecutor());
    }

    @Bean
    public Executor scheduledTaskExecutor() {
        return Executors.newScheduledThreadPool(PROPERTY_ASYNC_CORE_POOL_SIZE);
    }
}
