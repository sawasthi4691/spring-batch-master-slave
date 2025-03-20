package com.learn.batch;

import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@SpringBootApplication
@EnableBatchProcessing
public class BatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner runJob(JobLauncher jobLauncher, Job masterJob) {
//		return args -> {
//			jobLauncher.run(masterJob, new JobParameters());
//		};
//	}

//	@Bean
//	public Job simpleJob(JobRepository jobRepository, Step step1) {
//		return new JobBuilder("simpleJob", jobRepository)
//				.start(step1)
//				.build();
//	}
//
//	@Bean
//	public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
//		return new StepBuilder("step1", jobRepository)
//				.tasklet((contribution, chunkContext) -> {
//					System.out.println("Executing Batch Job...");
//					return RepeatStatus.FINISHED;
//				}, transactionManager)
//				.build();
//	}


}
