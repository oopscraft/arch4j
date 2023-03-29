package org.oopscraft.arch4j.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.oopscraft.arch4j.batch.sample.tasklet.CreateSampleTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseToDatabase extends AbstractBatchConfigurer {

    @Bean
    public Job job() {
        return getJobBuilder()
                .start(createSampleStep())
                .build();
    }

    @Bean
    @JobScope
    public Step createSampleStep() {
        return getStepBuilder("createSampleStep")
                .tasklet(createSampleTasklet(null))
                .transactionManager(getTransactionManager())
                .build();
    }

    @Bean
    @StepScope
    public CreateSampleTasklet createSampleTasklet(@Value("#{jobParameters[size]}") Long size) {
        return CreateSampleTasklet.builder()
                .size(size)
                .build();
    }

}
