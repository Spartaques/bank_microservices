package com.andriibashuk.tmservice.schedule;

import lombok.extern.java.Log;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
@Log
public class SampleJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(MessageFormat.format("Job: {0}", getClass()));
    }
}
