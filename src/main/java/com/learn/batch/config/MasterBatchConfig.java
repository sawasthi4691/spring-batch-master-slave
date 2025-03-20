package com.learn.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Configuration
@EnableBatchProcessing
public class MasterBatchConfig {

    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    // TaskExecutor for parallel processing
    @Bean
    public TaskExecutor taskExecutor(){
        return new SimpleAsyncTaskExecutor("batch_worker");
    }

    //Master Step: Partitions the data and assigns work to slave step
    @Bean
    public Step masterStep(Partitioner partitioner, Step slaveStep) {
        return  new StepBuilder("masterStep", jobRepository)
                .partitioner("slaveStep", partitioner)
                .step(slaveStep)
                .gridSize(4)
                .taskExecutor(taskExecutor())
                .build();
    }

    //Master Job: Runs the master Step
    @Bean
    public Job masterJob(Step masterStep) {
        return new JobBuilder("masterJob", jobRepository)
                .start(masterStep)
                .build();
    }

    //Partitioner: Divides CSV data into chunks
    @Bean
    public Partitioner partitioner() throws IOException {
        return gridSize -> {
            Map<String, ExecutionContext> partitions = new HashMap<>();

            // Read total records count (excluding header)
            int totalRows = getTotalRecords("input.csv") - 1; // Subtract header
            int partitionSize = 2; // Fixed size per partition (matching chunk size)

            int partitionIndex = 0;
            for (int startRow = 1; startRow <= totalRows; startRow += partitionSize) {
                ExecutionContext executionContext = new ExecutionContext();
                int endRow = Math.min(startRow + partitionSize - 1, totalRows);

                executionContext.putInt("startRow", startRow);
                executionContext.putInt("endRow", endRow);

                partitions.put("partition" + partitionIndex, executionContext);
                partitionIndex++;
                System.out.println("Partition " + partitionIndex + ": StartRow = " + startRow + ", EndRow = " + endRow);

            }

            return partitions;
        };
    }

    // Helper method to count lines in the file
    private int getTotalRecords(String fileName) {
        try (Stream<String> lines = Files.lines(Paths.get("src/main/resources/" + fileName))) {
            return (int) lines.count();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + fileName, e);
        }
    }
}
