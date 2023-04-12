package com.andriibashuk.tmservice.schedule;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

@Configuration
public class Config {
    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(SampleJob.class)
                .storeDurably()
                .withIdentity("tm.handletimings")
                .withDescription("Handle timings...")
                .build();
    }

    @Bean
    public Trigger trigger(JobDetail job) {
        return TriggerBuilder.newTrigger().forJob(job)
                .withIdentity("tm.handletimings")
                .withDescription("Handle timings...")
                .withSchedule(simpleSchedule().repeatForever().withIntervalInHours(1))
                .build();
    }
}
