package org.oopscraft.arch4j.core.common.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.oopscraft.arch4j.core.CoreConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.ScheduledTask;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Slf4j
@SpringBootTest(
        classes = CoreConfiguration.class,
        properties = {
                "spring.main.lazy-initialization=true",
                "spring.main.web-application-type=none",
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@TestPropertySource(properties = {
    "spring.h2.console.enabled=false"
})
@Import(CoreConfiguration.class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
public abstract class CoreTestSupport {

    @Autowired(required = false)
    private ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor;

    @Autowired
    @PersistenceContext
    protected EntityManager entityManager;

    @BeforeEach
    void cancelScheduledTasks() {
        if(scheduledAnnotationBeanPostProcessor != null) {
            scheduledAnnotationBeanPostProcessor.getScheduledTasks()
                    .forEach(ScheduledTask::cancel);
        }
    }

}
