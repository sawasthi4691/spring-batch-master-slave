package com.learn.batch.service;


import com.learn.batch.model.CsvFileData;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class PartitionedCsvFileReader extends FlatFileItemReader<CsvFileData> implements ItemStream {

    private final int startRow;
    private final int endRow;
    private int currentRow = 0; // Track the current row being read

    public PartitionedCsvFileReader(
            @Value("#{stepExecutionContext['startRow']}") Integer startRow,
            @Value("#{stepExecutionContext['endRow']}") Integer endRow) {
        this.startRow = startRow;
        this.endRow = endRow;

        // Set the CSV file as input
        setResource(new ClassPathResource("input.csv"));

        // Set row skipping to 1 (to skip only the header)
        setLinesToSkip(1);

        // Define how the CSV is mapped to the Java object
        setLineMapper(new DefaultLineMapper<>() {{
            setLineTokenizer(new DelimitedLineTokenizer() {{
                setNames("firstname", "lastname", "age");
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                setTargetType(CsvFileData.class);
            }});
        }});
    }

    @Override
    public CsvFileData read() throws Exception {
        CsvFileData item;
        while ((item = super.read()) != null) {
            currentRow++; // Track the row number
            if (currentRow >= startRow && currentRow <= endRow) {
                return item; // Process only rows in the partition range
            } else if (currentRow > endRow) {
                return null; // Stop reading if past the partition range
            }
        }
        return null; // End of file
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        super.open(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        super.close();
    }

}