package org.oopscraft.arch4j.batch.sample;

import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.batch.test.BatchTestSupport;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.batch.runtime.BatchStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes=DatabaseToDatabase.class)
public class DatabaseToDatabaseTest extends BatchTestSupport {

    @Test
    public void testJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("size", 123L)
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
    }

}
