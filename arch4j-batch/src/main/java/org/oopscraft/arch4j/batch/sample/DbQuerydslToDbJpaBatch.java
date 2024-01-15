package org.oopscraft.arch4j.batch.sample;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.oopscraft.arch4j.batch.item.database.QuerydslDbItemReader;
import org.oopscraft.arch4j.batch.item.database.QuerydslDbItemReaderBuilder;
import org.oopscraft.arch4j.batch.sample.tasklet.CompareSampleDbToSampleBackupDbTasklet;
import org.oopscraft.arch4j.batch.sample.tasklet.CreateSampleDbTasklet;
import org.oopscraft.arch4j.core.sample.dao.QSampleEntity;
import org.oopscraft.arch4j.core.sample.dao.SampleBackupEntity;
import org.oopscraft.arch4j.core.sample.dao.SampleEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DbQuerydslToDbJpaBatch extends AbstractBatchConfigurer {

    private final ModelMapper modelMapper;

    @Bean
    Job dbQuerydslToDbJpaJob() {
        return getJobBuilder("dbQuerydslToDbJpaJob")
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
                .<SampleEntity, SampleBackupEntity>chunk(10)
                .reader(sampleDbReader(null))
                .processor(convertSampleToSampleBackupProcessor())
                .writer(sampleBackupDbWriter())
                .build();
    }

    @Bean
    @StepScope
    QuerydslDbItemReader<SampleEntity> sampleDbReader(@Value("#{jobParameters[size]}") Integer size) {
        QSampleEntity qSample = QSampleEntity.sampleEntity;
        JPAQuery<SampleEntity> query = getJpaQueryFactory()
                .select(qSample)
                .from(qSample)
                .limit(size);
        return new QuerydslDbItemReaderBuilder<SampleEntity>()
                .entityManagerFactory(getEntityManagerFactory())
                .query(query)
                .build();
    }

    @Bean
    @StepScope
    ItemProcessor<SampleEntity, SampleBackupEntity> convertSampleToSampleBackupProcessor() {
        return sample -> {
            log.debug("sample:{}", sample);
            SampleBackupEntity sampleBackup = modelMapper.map(sample, SampleBackupEntity.class);
            log.debug("sampleBackup:{}", sampleBackup);
            return sampleBackup;
        };
    }

    @Bean
    @StepScope
    JpaItemWriter<SampleBackupEntity> sampleBackupDbWriter() {
        return new JpaItemWriterBuilder<SampleBackupEntity>()
                .entityManagerFactory(getEntityManagerFactory())
                .usePersist(true)
                .build();
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
