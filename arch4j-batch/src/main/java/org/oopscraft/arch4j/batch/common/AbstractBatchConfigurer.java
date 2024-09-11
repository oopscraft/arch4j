package org.oopscraft.arch4j.batch.common;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Getter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.oopscraft.arch4j.batch.common.listener.JobListener;
import org.oopscraft.arch4j.batch.common.listener.StepListener;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

@Configuration
public abstract class AbstractBatchConfigurer {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Getter
    private DataSource dataSource;

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

    @Autowired
    @Getter
    private SqlSessionFactory sqlSessionFactory;

    protected final JobBuilder getJobBuilder(String jobName) {
        return jobBuilderFactory
                .get(jobName)
                .listener(new JobListener());
    }

    protected final StepBuilder getStepBuilder(String stepName) {
        return stepBuilderFactory
                .get(stepName)
                .listener(new StepListener())
                .transactionManager(transactionManager);
    }

}
