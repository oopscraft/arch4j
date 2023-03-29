package org.oopscraft.arch4j.batch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Configuration
public abstract class AbstractBatchConfigurer {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Getter
    private PlatformTransactionManager transactionManager;

    @PersistenceContext
    @Getter
    private EntityManager entityManager;

    @Bean
    public abstract Job job();

    protected final JobBuilder getJobBuilder() {
        Class<?> clazz = ClassUtils.getUserClass(this.getClass());
        return jobBuilderFactory
                .get(clazz.getName());
    }

    protected final StepBuilder getStepBuilder(String stepName) {
        return stepBuilderFactory
                .get(stepName)
                .transactionManager(transactionManager);
    }

}
