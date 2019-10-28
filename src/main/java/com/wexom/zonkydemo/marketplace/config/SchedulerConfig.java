package com.wexom.zonkydemo.marketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Configuration for scheduler.
 */
@Configuration
public class SchedulerConfig implements SchedulingConfigurer {

    private static final int POOL_SIZE = 1;
    private static final String THREAD_NAME_PREFIX = "my-scheduled-task-pool-";

    /**
     * Methos which initialize and configure scheduler responsible for retrieving new loans.
     *
     * @param scheduledTaskRegistrar scheduled task registrar
     */
    @Override
    public void configureTasks(final ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
        threadPoolTaskScheduler.initialize();

        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }
}
