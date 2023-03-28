package org.oopscraft.arch4j.batch.test;

import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.batch.BatchApplication;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@Slf4j
@SpringBootTest(
        classes = BatchApplication.class
)
@SpringBatchTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class BatchTestSupport {

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

}
