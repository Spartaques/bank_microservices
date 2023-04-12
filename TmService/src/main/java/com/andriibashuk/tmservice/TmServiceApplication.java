package com.andriibashuk.tmservice;

import com.andriibashuk.tmservice.schedule.SampleJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@SpringBootApplication
@EnableScheduling
public class TmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TmServiceApplication.class, args);
	}
}
