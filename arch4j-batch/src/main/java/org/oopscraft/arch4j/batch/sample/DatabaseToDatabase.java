package org.oopscraft.arch4j.batch.sample;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.oopscraft.arch4j.batch.item.database.MybatisCursorItemReader;
import org.oopscraft.arch4j.batch.item.database.MybatisItemWriter;
import org.oopscraft.arch4j.batch.item.database.QuerydslCursorItemReader;
import org.oopscraft.arch4j.batch.sample.mapper.SampleBackupMapper;
import org.oopscraft.arch4j.batch.sample.mapper.SampleMapper;
import org.oopscraft.arch4j.batch.sample.tasklet.CompareSampleDataToBackupDataTasklet;
import org.oopscraft.arch4j.batch.sample.tasklet.CreateSampleDataTasklet;
import org.oopscraft.arch4j.batch.sample.mapper.SampleBackupVo;
import org.oopscraft.arch4j.batch.sample.mapper.SampleVo;
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
public class DatabaseToDatabase extends AbstractBatchConfigurer {

    private final ModelMapper modelMapper;

    /**
     * querydsl to jpa
     * @return job
     */
    @Bean
    Job querydslToJpaJob() {
        return getJobBuilder("querydslToJpaJob")
                .start(createSampleDataStep())
                .next(querydslCursorItemReaderToJpaItemWriterStep())
                .next(compareSampleDataToBackupDateStep())
                .build();
    }

    /**
     * mybatis to mybatis
     * @return job
     */
    @Bean
    Job mybatisToMybatisJob() {
        return getJobBuilder("mybatisToMybatisJob")
                .start(createSampleDataStep())
                .next(mybatisCursorItemReaderToMybatisItemWriterStep())
                .next(compareSampleDataToBackupDateStep())
                .build();
    }

    @Bean
    @JobScope
    Step createSampleDataStep() {
        return getStepBuilder("createSampleDataStep")
                .tasklet(createSampleDataTasklet(null))
                .transactionManager(getTransactionManager())
                .build();
    }

    @Bean
    @StepScope
    CreateSampleDataTasklet createSampleDataTasklet(@Value("#{jobParameters[size]}") Integer size) {
        return CreateSampleDataTasklet.builder()
                .size(size)
                .build();
    }

    @Bean
    @JobScope
    Step querydslCursorItemReaderToJpaItemWriterStep() {
        return getStepBuilder("querydslCursorItemReaderToJpaItemWriterStep")
                .<SampleEntity, SampleBackupEntity>chunk(10)
                .reader(querydslCursorItemReader(null))
                .processor(sampleEntityToSampleBackupEntityProcessor())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    @StepScope
    QuerydslCursorItemReader<SampleEntity> querydslCursorItemReader(@Value("#{jobParameters[size]}") Integer size) {
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
    ItemProcessor<SampleEntity, SampleBackupEntity> sampleEntityToSampleBackupEntityProcessor() {
        return sample -> {
            log.debug("sample:{}", sample);
            SampleBackupEntity sampleBackup = modelMapper.map(sample, SampleBackupEntity.class);
            log.debug("sampleBackup:{}", sampleBackup);
            return sampleBackup;
        };
    }

    @Bean
    @StepScope
    JpaItemWriter<SampleBackupEntity> jpaItemWriter() {
        return new JpaItemWriterBuilder<SampleBackupEntity>()
                .entityManagerFactory(getEntityManagerFactory())
                .usePersist(true)
                .build();
    }

    @Bean
    @JobScope
    Step compareSampleDataToBackupDateStep() {
        return getStepBuilder("compareSampleDataToBackupDataStep")
                .tasklet(compareSampleDataToBackupDataTasklet())
                .build();
    }

    @Bean
    @StepScope
    CompareSampleDataToBackupDataTasklet compareSampleDataToBackupDataTasklet() {
        return CompareSampleDataToBackupDataTasklet.builder()
                .build();
    }

    @Bean
    @JobScope
    Step mybatisCursorItemReaderToMybatisItemWriterStep() {
        return getStepBuilder("mybatisCursorItemReaderToMybatisItemWriter")
                .<SampleVo, SampleBackupVo>chunk(10)
                .reader(mybatisCursorItemReader(null))
                .processor(sampleVoToSampleBackupVoProcessor())
                .writer(mybatisItemWriter())
                .build();
    }

    @Bean
    @StepScope
    MybatisCursorItemReader<SampleVo> mybatisCursorItemReader(@Value("#{jobParameters[size]}") Integer size) {
        return MybatisCursorItemReader.<SampleVo>builder()
                .dataSource(getDataSource())
                .sqlSessionFactory(getSqlSessionFactory())
                .mapperClass(SampleMapper.class)
                .mapperMethod("selectSamples")
                .parameter("limit", size)
                .build();
    }

    @Bean
    @StepScope
    ItemProcessor<SampleVo,SampleBackupVo> sampleVoToSampleBackupVoProcessor() {
        return sampleVo -> {
            log.debug("sampleVo:{}", sampleVo);
            SampleBackupVo sampleBackupVo = modelMapper.map(sampleVo, SampleBackupVo.class);
            log.debug("sampleBackupVo:{}", sampleBackupVo);
            return sampleBackupVo;
        };
    }

    @Bean
    @StepScope
    MybatisItemWriter<SampleBackupVo> mybatisItemWriter() {
        return MybatisItemWriter.<SampleBackupVo>builder()
                .sqlSessionFactory(getSqlSessionFactory())
                .mapperClass(SampleBackupMapper.class)
                .mapperMethod("insertSampleBackup")
                .build();
    }



}
