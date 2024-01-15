package org.oopscraft.arch4j.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.oopscraft.arch4j.batch.item.file.*;
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
    DelimiterFileItemReader<SampleFile> sampleFileReader(@Value("#{jobParameters[inputFile]}")String inputFile) {
        return new DelimiterFileItemReaderBuilder<SampleFile>()
                .itemType(SampleFile.class)
                .resource(new FileSystemResource(inputFile))
                .encoding("UTF-8")
                .lineSeparator("\n")
                .delimiter("\t")
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
    FixedByteFileItemWriter<SampleBackupFile> sampleBackupFileWriter(@Value("#{jobParameters[outputFile]}")String outputFile) {
        return new FixedByteFileItemWriterBuilder<SampleBackupFile>()
                .itemType(SampleBackupFile.class)
                .resource(new FileSystemResource(outputFile))
                .encoding("UTF-8")
                .lineSeparator("\n")
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
