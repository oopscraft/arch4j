package org.oopscraft.arch4j.batch.sample;

import org.oopscraft.arch4j.batch.support.BatchTestSupport;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes= DatabaseToFileBatch.class)
public class DatabaseToFileBatchTest extends BatchTestSupport {

//    @Test
//    public void testJob() throws Exception {
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("message", "hello world!")
//                .toJobParameters();
//        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
//        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
//    }

}
