package org.oopscraft.arch4j.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.mybatis.spring.batch.MyBatisBatchItemWriter;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.oopscraft.arch4j.batch.item.database.MybatisCursorItemReader;
import org.oopscraft.arch4j.batch.sample.dao.SampleBackupMapper;
import org.oopscraft.arch4j.batch.sample.dao.SampleBackupVo;
import org.oopscraft.arch4j.batch.sample.dao.SampleMapper;
import org.oopscraft.arch4j.batch.sample.dao.SampleVo;
import org.oopscraft.arch4j.batch.sample.tasklet.CompareSampleDbToSampleBackupDbTasklet;
import org.oopscraft.arch4j.batch.sample.tasklet.CreateSampleDbTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DbMybatisToDbMybatisBatch extends AbstractBatchConfigurer {

    private final ModelMapper modelMapper;

    @Bean
    Job dbMybatisToDbMybatisJob() {
        return getJobBuilder("dbMybatisToDbMybatisJob")
                .start(createSampleDbStep())
                .next(copySampleDbToSampleBackupDbStep())
                .next(compareSampleDbToSampleBackupDbStep())
                .build();
    }

    @Bean
    @JobScope
    Step createSampleDbStep() {
        return getStepBuilder("createSampleDbStep")
                .tasklet(createSampleDbTasklet(null))
                .transactionManager(getTransactionManager())
                .build();
    }

    @Bean
    @StepScope
    CreateSampleDbTasklet createSampleDbTasklet(@Value("#{jobParameters[size]}") Integer size) {
        return CreateSampleDbTasklet.builder()
                .size(size)
                .build();
    }

    @Bean
    @JobScope
    Step copySampleDbToSampleBackupDbStep() {
        return getStepBuilder("copySampleDbToSampleBackupDbStep")
                .<SampleVo, SampleBackupVo>chunk(10)
                .reader(sampleDbReader(null))
                .processor(convertSampleToSampleBackupProcessor())
                .writer(sampleBackupDbWriter())
                .build();
    }

    @Bean
    @StepScope
    MybatisCursorItemReader<SampleVo> sampleDbReader(@Value("#{jobParameters[size]}") Integer size) {
        MybatisCursorItemReader<SampleVo> itemReader = new MybatisCursorItemReader<>();
        itemReader.setSqlSessionFactory(getSqlSessionFactory());
        itemReader.setQueryId(SampleMapper.class.getName() + "." + "selectSamples");
        itemReader.setParameterValues(new HashMap<>(){{
            put("limit", size);
        }});
        return itemReader;
    }

    @Bean
    @StepScope
    ItemProcessor<SampleVo,SampleBackupVo> convertSampleToSampleBackupProcessor() {
        return sampleVo -> {
            log.debug("sample:{}", sampleVo);
            SampleBackupVo sampleBackupVo = modelMapper.map(sampleVo, SampleBackupVo.class);
            log.debug("sampleBackup:{}", sampleBackupVo);
            return sampleBackupVo;
        };
    }

    @Bean
    @StepScope
    MyBatisBatchItemWriter<SampleBackupVo> sampleBackupDbWriter() {
        MyBatisBatchItemWriter<SampleBackupVo> itemWriter = new MyBatisBatchItemWriter<>();
        itemWriter.setSqlSessionFactory(getSqlSessionFactory());
        itemWriter.setStatementId(SampleBackupMapper.class.getName() + "." + "insertSampleBackup");
        itemWriter.setAssertUpdates(false); // disable assert updates
        return itemWriter;
    }

    @Bean
    @JobScope
    Step compareSampleDbToSampleBackupDbStep() {
        return getStepBuilder("compareSampleDbToSampleBackupDbStep")
                .tasklet(compareSampleDataToSampleBackupDataTasklet())
                .build();
    }

    @Bean
    @StepScope
    CompareSampleDbToSampleBackupDbTasklet compareSampleDataToSampleBackupDataTasklet() {
        return CompareSampleDbToSampleBackupDbTasklet.builder()
                .build();
    }

}
