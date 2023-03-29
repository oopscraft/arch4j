package org.oopscraft.arch4j.batch.sample;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.oopscraft.arch4j.batch.item.database.QuerydslCursorItemReader;
import org.oopscraft.arch4j.batch.sample.tasklet.CreateSampleDataTasklet;
import org.oopscraft.arch4j.core.sample.entity.QSampleEntity;
import org.oopscraft.arch4j.core.sample.entity.SampleBackupEntity;
import org.oopscraft.arch4j.core.sample.entity.SampleEntity;
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
public class DatabaseToDatabase extends AbstractBatchConfigurer {

    private final ModelMapper modelMapper;

    @Bean
    public Job job() {
        return getJobBuilder()
                .start(createSampleDataStep())
                .next(querydslCursorItemReaderToJpaItemWriterStep())
                .build();
    }

    @Bean
    @JobScope
    public Step createSampleDataStep() {
        return getStepBuilder("createSampleDataStep")
                .tasklet(createSampleDataTasklet(null))
                .transactionManager(getTransactionManager())
                .build();
    }

    @Bean
    @StepScope
    public CreateSampleDataTasklet createSampleDataTasklet(@Value("#{jobParameters[size]}") Long size) {
        return CreateSampleDataTasklet.builder()
                .size(size)
                .build();
    }

    @Bean
    @JobScope
    public Step querydslCursorItemReaderToJpaItemWriterStep() {
        return getStepBuilder("querydslCursorItemReaderToJpaItemWriterStep")
                .<SampleEntity, SampleBackupEntity>chunk(10)
                .reader(querydslCursorItemReader(null))
                .processor(sampleToSampleBackupProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public QuerydslCursorItemReader<SampleEntity> querydslCursorItemReader(@Value("#{jobParameters[size]}") Long size) {
        QSampleEntity qSample = QSampleEntity.sampleEntity;
        JPAQuery<SampleEntity> query = getJpaQueryFactory()
                .select(qSample)
                .from(qSample)
                .limit(size);
        return QuerydslCursorItemReader.<SampleEntity>builder()
                .entityManagerFactory(getEntityManagerFactory())
                .query(query)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<SampleEntity, SampleBackupEntity> sampleToSampleBackupProcessor() {
        return sample -> {
            log.debug("sample:{}", sample);
            SampleBackupEntity sampleBackup = modelMapper.map(sample, SampleBackupEntity.class);
            log.debug("sampleBackup:{}", sampleBackup);
            return sampleBackup;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<SampleBackupEntity> jpaItemWriter() {
        return new JpaItemWriterBuilder<SampleBackupEntity>()
                .entityManagerFactory(getEntityManagerFactory())
                .usePersist(true)
                .build();
    }


}
