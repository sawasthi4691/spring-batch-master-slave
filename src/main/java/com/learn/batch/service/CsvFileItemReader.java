package com.learn.batch.service;

import com.learn.batch.model.CsvFileData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CsvFileItemReader {

    @Bean
    @StepScope
    public ItemReader<CsvFileData> partitionedReader(@Value("#{stepExecutionContext['startRow']}") Integer startRow,
                                                     @Value("#{stepExecutionContext['endRow']}") Integer endRow) {
        return new PartitionedCsvFileReader(startRow, endRow);
    }


}
