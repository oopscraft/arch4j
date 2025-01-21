package org.oopscraft.arch4j.batch.sample;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.batch.common.support.BatchTestSupport;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ContextConfiguration(classes= DbMybatisToDbMybatisBatch.class)
public class DbMybatisToDbMybatisBatchTest extends BatchTestSupport {

    @Autowired
    @Qualifier("dbMybatisToDbMybatisJob")
    Job mybatisToMybatisJob;

//    @Test
    void dbMybatisToDbMybatisJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("size", 1234L)
                .toJobParameters();
        JobExecution jobExecution = getJobLauncherTestUtils(mybatisToMybatisJob).launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
    }

}
