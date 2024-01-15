package org.oopscraft.arch4j.batch.sample;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.batch.support.BatchTestSupport;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import javax.batch.runtime.BatchStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ContextConfiguration(classes= FileDsvToFileFldBatch.class)
public class FileDsvToFileFldBatchTest extends BatchTestSupport {

    @Autowired
    @Qualifier("fileDsvToFileFldJob")
    Job fileDsvToFileFldJob;

    @Test
    void fileDsvToFileFldJob() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("size", 1234L)
                .addString("lang", "ko")
                .addString("inputFile", getDataDirectory(this.getClass()) + "fileDsvToFileFld.input.dsv")
                .addString("outputFile", getDataDirectory(this.getClass()) + "fileDsvToFileFld.output.fld")
                .toJobParameters();
        JobExecution jobExecution = getJobLauncherTestUtils(fileDsvToFileFldJob).launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
    }

    @Test
    void fileDsvToFileFldJobWithLineSeparator() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("size", 1234L)
                .addString("lang", "ko")
                .addString("inputFile", "./.tmp/sample/fileDsvToFileFld.sample.dsv")
                .addString("inputLineSeparator", "\n")
                .addString("inputEncoding", "utf-8")
                .addString("inputDelimiter", "\t")
                .addString("outputFile", "./.tmp/sample/fileDsvToFileFld.sampleBackup.fld")
                .addString("outputEncoding", "euc-kr")
                .addString("outputLineSeparator", "\n")
                .toJobParameters();
        JobExecution jobExecution = getJobLauncherTestUtils(fileDsvToFileFldJob).launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED.name(), jobExecution.getStatus().name());
    }


}
