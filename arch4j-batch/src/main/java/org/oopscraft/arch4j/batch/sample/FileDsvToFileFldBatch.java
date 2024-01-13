package org.oopscraft.arch4j.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileDsvToFileFldBatch extends AbstractBatchConfigurer {

    private final ModelMapper modelMapper;

//    @Bean
//    Job mybatisToMybatisJob() {
//        return getJobBuilder("mybatisToMybatisJob")
//                .start(createSampleDataStep())
//                .next(mybatisCursorItemReaderToMybatisItemWriterStep())
//                .next(compareSampleDataToBackupDateStep())
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    Step createSampleDataStep() {
//        return getStepBuilder("createSampleDataStep")
//                .tasklet(createSampleDataTasklet(null))
//                .transactionManager(getTransactionManager())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    CreateSampleDbTasklet createSampleDataTasklet(@Value("#{jobParameters[size]}") Integer size) {
//        return CreateSampleDbTasklet.builder()
//                .size(size)
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    Step mybatisCursorItemReaderToMybatisItemWriterStep() {
//        return getStepBuilder("mybatisCursorItemReaderToMybatisItemWriter")
//                .<SampleVo, SampleBackupVo>chunk(10)
//                .reader(mybatisCursorItemReader(null))
//                .processor(sampleVoToSampleBackupVoProcessor())
//                .writer(mybatisItemWriter())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    MybatisCursorItemReader<SampleVo> mybatisCursorItemReader(@Value("#{jobParameters[size]}") Integer size) {
//        return MybatisCursorItemReader.<SampleVo>builder()
//                .dataSource(getDataSource())
//                .sqlSessionFactory(getSqlSessionFactory())
//                .mapperClass(SampleMapper.class)
//                .mapperMethod("selectSamples")
//                .parameter("limit", size)
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    ItemProcessor<SampleVo,SampleBackupVo> sampleVoToSampleBackupVoProcessor() {
//        return sampleVo -> {
//            log.debug("sampleVo:{}", sampleVo);
//            SampleBackupVo sampleBackupVo = modelMapper.map(sampleVo, SampleBackupVo.class);
//            log.debug("sampleBackupVo:{}", sampleBackupVo);
//            return sampleBackupVo;
//        };
//    }
//
//    @Bean
//    @StepScope
//    MybatisItemWriter<SampleBackupVo> mybatisItemWriter() {
//        return MybatisItemWriter.<SampleBackupVo>builder()
//                .sqlSessionFactory(getSqlSessionFactory())
//                .mapperClass(SampleBackupMapper.class)
//                .mapperMethod("insertSampleBackup")
//                .build();
//    }
//
//    @Bean
//    @JobScope
//    Step compareSampleDataToBackupDateStep() {
//        return getStepBuilder("compareSampleDataToBackupDataStep")
//                .tasklet(compareSampleDataToBackupDataTasklet())
//                .build();
//    }
//
//    @Bean
//    @StepScope
//    CompareSampleDbToSampleBackupDbTasklet compareSampleDataToBackupDataTasklet() {
//        return CompareSampleDbToSampleBackupDbTasklet.builder()
//                .build();
//    }

}
