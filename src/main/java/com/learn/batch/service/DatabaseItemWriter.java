package com.learn.batch.service;

import com.learn.batch.model.CsvProcessedData;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DatabaseItemWriter {

    @Bean
    public JdbcBatchItemWriter<CsvProcessedData> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<CsvProcessedData>()
                .dataSource(dataSource)
                .sql("INSERT INTO CSV_PROCESSED_DATA (FIRSTNAME, LASTNAME, AGE) values (:firstname, :lastname, :age)")
                .beanMapped()
                .build();

    }
}
