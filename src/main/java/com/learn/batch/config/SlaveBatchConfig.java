package com.learn.batch.config;

import com.learn.batch.model.CsvFileData;
import com.learn.batch.model.CsvProcessedData;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class SlaveBatchConfig {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    //Slave step: Read CSV partitions, processes, and writes to H2 DB.
    @Bean
    public Step slaveStep(@Qualifier("partitionedCsvFileReader") ItemReader<CsvFileData> reader, ItemProcessor<CsvFileData, CsvProcessedData> processor,
                          ItemWriter<CsvProcessedData> writer) {
        return new StepBuilder("slaveStep", jobRepository)
                .<CsvFileData, CsvProcessedData>chunk(2, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(new StepExecutionListenerSupport() {
                    @Override
                    public void beforeStep(StepExecution stepExecution) {
                        System.out.println("Starting slave job..." + stepExecution.getStepName() + " " + stepExecution.getCommitCount());
                    }
                    @Override
                    public ExitStatus afterStep(StepExecution stepExecution) {
                        System.out.println("Completed slave job." + stepExecution.getStepName() +
                                " Write count " + stepExecution.getWriteCount() + "summary :  " + stepExecution.getSummary());
                        return stepExecution.getExitStatus();
                    }
                })
                .build();
    }


    @Bean
    public Job slaveJob(Step slaveStep) {
        return new JobBuilder("slaveJob", jobRepository)
                .start(slaveStep)
                .build();
    }
}
