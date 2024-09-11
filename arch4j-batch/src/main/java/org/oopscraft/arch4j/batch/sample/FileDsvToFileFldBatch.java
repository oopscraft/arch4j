package org.oopscraft.arch4j.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.batch.common.AbstractBatchConfigurer;
import org.oopscraft.arch4j.batch.common.item.file.DelimiterFileItemReader;
import org.oopscraft.arch4j.batch.common.item.file.DelimiterFileItemReaderBuilder;
import org.oopscraft.arch4j.batch.common.item.file.FixedByteFileItemWriter;
import org.oopscraft.arch4j.batch.common.item.file.FixedByteFileItemWriterBuilder;
import org.oopscraft.arch4j.batch.sample.dto.SampleBackupFile;
import org.oopscraft.arch4j.batch.sample.dto.SampleFile;
import org.oopscraft.arch4j.batch.sample.tasklet.CompareSampleFileToSampleBackupFileTasklet;
import org.oopscraft.arch4j.batch.sample.tasklet.CreateSampleFileTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Optional;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileDsvToFileFldBatch extends AbstractBatchConfigurer {

    private final ModelMapper modelMapper;

    @Bean
    Job fileDsvToFileFldJob() {
        return getJobBuilder("fileDsvToFileFldJob")
                .start(createSampleFileStep())
                .next(copySampleFileToSampleBackupFileStep())
//                .next(compareSampleFileToSampleBackupFileStep())
                .build();
    }

    @Bean
    @JobScope
    Step createSampleFileStep() {
        return getStepBuilder("createSampleFileStep")
                .tasklet(createSampleFileTasklet(null, null, null, null, null, null))
                .transactionManager(getTransactionManager())
                .build();
    }

    @Bean
    @StepScope
    CreateSampleFileTasklet createSampleFileTasklet(
            @Value("#{jobParameters[size]}")Integer size,
            @Value("#{jobParameters[lang]}")String lang,
            @Value("#{jobParameters[inputFile]}")String inputFile,
            @Value("#{jobParameters[inputEncoding]}")String inputEncoding,
            @Value("#{jobParameters[inputLineSeparator]}")String inputLineSeparator,
            @Value("#{jobParameters[inputDelimiter]}")String inputDelimiter
    ) {
        return CreateSampleFileTasklet.builder()
                .size(size)
                .lang(lang)
                .resource(new FileSystemResource(inputFile))
                .fileType(CreateSampleFileTasklet.FileType.DSV)
                .encoding(Optional.ofNullable(inputEncoding).orElse("utf-8"))
                .lineSeparator(Optional.ofNullable(inputLineSeparator).orElse("\n"))
                .delimiter(Optional.ofNullable(inputDelimiter).orElse("\t"))
                .build();
    }

    @Bean
    @JobScope
    Step copySampleFileToSampleBackupFileStep() {
        return getStepBuilder("copySampleFileToSampleBackupFileStep")
                .<SampleFile, SampleBackupFile>chunk(10)
                .reader(sampleFileReader(null, null, null, null))
                .processor(convertSampleToSampleBackupProcessor())
                .writer(sampleBackupFileWriter(null, null, null))
                .build();
    }

    @Bean
    @StepScope
    DelimiterFileItemReader<SampleFile> sampleFileReader(
            @Value("#{jobParameters[inputFile]}") String inputFile,
            @Value("#{jobParameters[inputEncoding]}") String inputEncoding,
            @Value("#{jobParameters[inputLineSeparator]}") String inputLineSeparator,
            @Value("#{jobParameters[inputDelimiter]}") String inputDelimiter
    ) {
        return new DelimiterFileItemReaderBuilder<SampleFile>()
                .itemType(SampleFile.class)
                .resource(new FileSystemResource(inputFile))
                .encoding(Optional.ofNullable(inputEncoding).orElse("utf-8"))
                .lineSeparator(Optional.ofNullable(inputLineSeparator).orElse("\n"))
                .delimiter(Optional.ofNullable(inputDelimiter).orElse("\t"))
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
    FixedByteFileItemWriter<SampleBackupFile> sampleBackupFileWriter(
            @Value("#{jobParameters[outputFile]}") String outputFile,
            @Value("#{jobParameters[outputEncoding]}") String outputEncoding,
            @Value("#{jobParameters[outputLineSeparator]}") String outputLineSeparator
    ) {
        return new FixedByteFileItemWriterBuilder<SampleBackupFile>()
                .itemType(SampleBackupFile.class)
                .resource(new FileSystemResource(outputFile))
                .encoding(Optional.ofNullable(outputEncoding).orElse("utf-8"))
                .lineSeparator(Optional.ofNullable(outputLineSeparator).orElse("\n"))
                .build();
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
