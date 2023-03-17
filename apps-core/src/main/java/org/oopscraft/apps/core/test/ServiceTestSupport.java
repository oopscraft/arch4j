package org.oopscraft.apps.core.test;

import lombok.extern.slf4j.Slf4j;
import org.oopscraft.apps.core.CoreConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest(
        classes = {CoreConfiguration.class},
        properties = {
                "spring.main.lazy-initialization=true",
                "spring.main.web-application-type=none"
        }
)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
@Rollback
public abstract class ServiceTestSupport {

}
