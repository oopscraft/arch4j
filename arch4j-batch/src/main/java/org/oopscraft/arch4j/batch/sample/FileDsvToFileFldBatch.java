package org.oopscraft.arch4j.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.oopscraft.arch4j.batch.item.database.MybatisCursorItemReader;
import org.oopscraft.arch4j.batch.item.file.transform.FixedByteLineAggregator;
import org.oopscraft.arch4j.batch.sample.dao.SampleBackupMapper;
import org.oopscraft.arch4j.batch.sample.dao.SampleBackupVo;
import org.oopscraft.arch4j.batch.sample.dao.SampleMapper;
import org.oopscraft.arch4j.batch.sample.dao.SampleVo;
import org.oopscraft.arch4j.batch.sample.dto.SampleBackupFile;
import org.oopscraft.arch4j.batch.sample.dto.SampleFile;
import org.oopscraft.arch4j.batch.sample.tasklet.CompareSampleDbToSampleBackupDbTasklet;
import org.oopscraft.arch4j.batch.sample.tasklet.CompareSampleFileToSampleBackupFileTasklet;
import org.oopscraft.arch4j.batch.sample.tasklet.CreateSampleFileTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FormatterLineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileDsvToFileFldBatch extends AbstractBatchConfigurer {

    private final ModelMapper modelMapper;

    @Bean
    Job fileDsvToFileFldJob() {
        return getJobBuilder("fileDsvToFileFldJob")
                .start(createSampleFileStep())
//                .next(copySampleFileToSampleBackupFileStep())
//                .next(compareSampleFileToSampleBackupFileStep())
                .build();
    }

    @Bean
    @JobScope
    Step createSampleFileStep() {
        return getStepBuilder("createSampleFileStep")
                .tasklet(createSampleFileTasklet(null, null))
                .transactionManager(getTransactionManager())
                .build();
    }

    @Bean
    @StepScope
    CreateSampleFileTasklet createSampleFileTasklet(@Value("#{jobParameters[size]}")Integer size, @Value("#{jobParameters[inputFile]}") String inputFile) {
        return CreateSampleFileTasklet.builder()
                .size(size)
                .resource(new FileSystemResource(inputFile))
                .fileType(CreateSampleFileTasklet.FileType.DSV)
                .encoding("UTF-8")
                .lineSeparator("\n")
                .delimiter("\t")
                .build();
    }

    @Bean
    @JobScope
    Step copySampleFileToSampleBackupFileStep() {
        return getStepBuilder("copySampleFileToSampleBackupFileStep")
                .<SampleFile, SampleBackupFile>chunk(10)
                .reader(sampleFileReader(null))
                .processor(convertSampleToSampleBackupProcessor())
                .writer(sampleBackupFileWriter(null))
                .build();
    }

    @Bean
    @StepScope
    FlatFileItemReader<SampleFile> sampleFileReader(@Value("#{jobParameters[inputFile]}")String inputFile) {
        // line tokenizer
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("\t");
        List<String> fieldNames = new ArrayList<>();
        ReflectionUtils.doWithFields(SampleFile.class, field -> fieldNames.add(field.getName()));
        lineTokenizer.setNames(fieldNames.toArray(new String[0]));

        // line mapper
        BeanWrapperFieldSetMapper<SampleFile> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(SampleFile.class);

        // flat file item writer
        return new FlatFileItemReaderBuilder<SampleFile>()
                .resource(new FileSystemResource(inputFile))
                .lineTokenizer(lineTokenizer)
                .fieldSetMapper(fieldSetMapper)
                .build();
    }

    @Bean
    @StepScope
    ItemProcessor<SampleFile, SampleBackupFile> convertSampleToSampleBackupProcessor() {
        return sampleFile -> {
            log.debug("sample:{}", sampleFile);
            SampleBackupFile sampleBackupFile = modelMapper.map(sampleFile, SampleBackupFile.class);
            log.debug("sampleBackup:{}", sampleBackupFile);
            return sampleBackupFile;
        };
    }

    @Bean
    @StepScope
    FlatFileItemWriter<SampleBackupFile> sampleBackupFileWriter(@Value("#{jobParameters[outputFile]}")String outputFile) {
        // field extractor
        BeanWrapperFieldExtractor<SampleBackupFile> fieldExtractor = new BeanWrapperFieldExtractor<>();
        List<String> fieldNames = new ArrayList<>();
        ReflectionUtils.doWithFields(SampleFile.class, field -> fieldNames.add(field.getName()));
        fieldExtractor.setNames(fieldNames.toArray(new String[0]));

        // line aggregator
        FormatterLineAggregator<SampleBackupFile> lineAggregator = new FormatterLineAggregator<>();
        lineAggregator.setFieldExtractor(fieldExtractor);
        lineAggregator.setFormat("%s");



        FlatFileItemWriter<SampleBackupFile> itemWriter = new FlatFileItemWriter();
        itemWriter.setResource(new FileSystemResource(outputFile));
        return itemWriter;
    }

    @Bean
    @JobScope
    Step compareSampleFileToSampleBackupFileStep() {
        return getStepBuilder("compareSampleDbToSampleBackupDbStep")
                .tasklet(compareSampleFileToSampleBackupFileTasklet())
                .build();
    }

    @Bean
    @StepScope
    CompareSampleFileToSampleBackupFileTasklet compareSampleFileToSampleBackupFileTasklet() {
        return CompareSampleFileToSampleBackupFileTasklet.builder()
                .build();
    }

}
