package org.chomoo.arch4j.batch.sample.tasklet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.chomoo.arch4j.core.sample.dao.*;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import jakarta.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.Optional;

@Slf4j
@Builder
public class CompareSampleFileToSampleBackupFileTasklet implements Tasklet {

    public static enum FileType {
        DSV, FLD
    }

    @NotNull
    private Resource sampleResource;

    @Builder.Default
    private FileType sampleFileType = FileType.DSV;

    @Builder.Default
    private String sampleEncoding = StandardCharsets.UTF_8.name();

    @Builder.Default
    private String sampleLineSeparator = String.valueOf('\n');

    @Builder.Default
    private String sampleDelimiter = String.valueOf('\t');

    @NotNull
    private Resource sampleBackupResource;

    @Builder.Default
    private FileType sampleBackupFileType = FileType.DSV;

    @Builder.Default
    private String sampleBackupEncoding = StandardCharsets.UTF_8.name();

    @Builder.Default
    private String sampleBackupLineSeparator = String.valueOf('\n');

    @Builder.Default
    private String sampleBackupDelimiter = String.valueOf('\t');

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        // return
        return RepeatStatus.FINISHED;
    }
}
