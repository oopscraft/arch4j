package org.oopscraft.arch4j.batch.sample;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.batch.AbstractBatchConfigurer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseToDatabase extends AbstractBatchConfigurer {

    @Bean
    public Job job() {
        return getJobBuilder()
                .start(testStep())
                .build();
    }

    @Bean
    public Step testStep() {
        return getStepBuilder("testStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("== {}", this.getClass().getName());
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
