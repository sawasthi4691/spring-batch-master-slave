package com.learn.batch.service;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobLauncherService {

    @Autowired
    private JobLauncher jobLauncher; // Inject the JobLauncher

    @Autowired
    private Job masterJob; // Inject your master job

    public void runMasterJob() {
        try {
            // Create JobParameters, you can add any required parameters here
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("jobName", "masterJob")
                    .addLong("timestamp", System.currentTimeMillis()) // Unique timestamp for each run
                    .toJobParameters();
            // Launch the job
            JobExecution jobExecution = jobLauncher.run(masterJob, jobParameters);
            System.out.println("Job Status : " + jobExecution.getStatus()); // Print job status
        } catch (Exception e) {
            e.printStackTrace(); // Handle any exceptions that occur during job execution
        }
    }
}

