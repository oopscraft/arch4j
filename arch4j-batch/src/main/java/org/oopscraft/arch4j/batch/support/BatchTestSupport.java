package org.oopscraft.arch4j.batch.support;

import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.batch.BatchConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import java.io.File;

@Slf4j
@SpringBootTest(
        classes = BatchConfiguration.class
)
@SpringBatchTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class BatchTestSupport {

    private static final String DATA_HOME = "./.tmp/";

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobLauncher jobLauncher;

    protected final JobLauncherTestUtils getJobLauncherTestUtils(Job job) {
        JobLauncherTestUtils jobLauncherTestUtils = new JobLauncherTestUtils();
        jobLauncherTestUtils.setJobRepository(jobRepository);
        jobLauncherTestUtils.setJobLauncher(jobLauncher);
        jobLauncherTestUtils.setJob(job);
        return jobLauncherTestUtils;
    }

    protected final String getDataDirectory(Class<?> batchClass) {
        return  DATA_HOME + batchClass.getName().replaceAll("\\.", File.separator) + File.separator;
    }

}
