package org.chomoo.arch4j.web.common.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.chomoo.arch4j.web.WebConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import jakarta.persistence.EntityManager;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(
        classes = WebConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.h2.console.enabled=false",
                "springdoc.swagger-ui.enabled=false",
                "spring.scheduling.enabled=false"
        }
)
@AutoConfigureMockMvc(addFilters = false)
@WithUserDetails(value="admin")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@Rollback
@Slf4j
public class WebTestSupport {

    @Autowired(required = false)
    private ScheduledAnnotationBeanPostProcessor scheduledAnnotationBeanPostProcessor;

    @Autowired
    @Getter
    protected WebApplicationContext webApplicationContext;

    @Autowired
    @Getter
    protected MockMvc mockMvc;

    @Autowired
    @Getter
    protected ObjectMapper objectMapper;

    @Autowired
    @Getter
    protected EntityManager entityManager;

}
