package org.oopscraft.arch4j.batch;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import org.oopscraft.arch4j.batch.listener.StepListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

    @Autowired
    @Getter
    private EntityManagerFactory entityManagerFactory;

    @PersistenceContext
    @Getter
    private EntityManager entityManager;

    @Autowired
    @Getter
    private JPAQueryFactory jpaQueryFactory;

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
                .listener(new StepListener())
                .transactionManager(transactionManager);
    }

}
