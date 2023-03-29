package org.oopscraft.arch4j.batch.sample;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.batch.test.BatchTestSupport;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import javax.batch.runtime.BatchStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes=DatabaseToFile.class)
public class DatabaseToFileTest extends BatchTestSupport {

//    @Test
//    public void testJob() throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("message", "hello world!")
//                .toJobParameters();
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
//        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
//    }

}
