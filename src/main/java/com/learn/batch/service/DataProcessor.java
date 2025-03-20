package com.learn.batch.service;

import com.learn.batch.model.CsvFileData;
import com.learn.batch.model.CsvProcessedData;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class DataProcessor implements ItemProcessor<CsvFileData, CsvProcessedData> {

    @Override
    public CsvProcessedData process(CsvFileData item) throws Exception {
        System.out.println("Processing item: " + item);
        CsvProcessedData processedData = new CsvProcessedData();
        processedData.setFirstname(item.getFirstname());
        processedData.setLastname(item.getLastname());
        processedData.setAge(item.getAge());
        return processedData;
    }
}
