package org.chomoo.arch4j.batch.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.batch.core.*;
import org.springframework.batch.core.annotation.*;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class StepListener implements StepExecutionListener, ChunkListener {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final StopWatch stepStopWatch = new StopWatch();

    private int chunkCount = 0;

    private final StopWatch chunkStopWatch = new StopWatch();

    public static Object toObject() {
        return new StepListener();
    }

    @BeforeStep
    @Override
    public final void beforeStep(StepExecution stepExecution) {
        log.info("-".repeat(80));
        log.info("| [START] StepExecution");
        log.info("| stepName: {}", stepExecution.getStepName());
        log.info("| startTime: {}", Optional.ofNullable(stepExecution.getStartTime()).map(v->DATE_FORMAT.format(v)).orElse(null));
        log.info("-".repeat(80));
        stepStopWatch.start();
    }

    @BeforeChunk
    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        chunkCount ++;
        chunkStopWatch.reset();
        chunkStopWatch.start();
    }

    @AfterChunk
    @Override
    public void afterChunk(ChunkContext chunkContext) {
        chunkStopWatch.stop();
        StepExecution stepExecution = chunkContext.getStepContext().getStepExecution();
        String chunkMessage = String.format("[%d](%dms) stepName=%s, readCount=%d, filterCount=%d, writeCount=%d, commitCount=%d"
                ,chunkCount
                ,chunkStopWatch.getTime(TimeUnit.MILLISECONDS)
                ,stepExecution.getStepName()
                ,stepExecution.getReadCount()
                ,stepExecution.getFilterCount()
                ,stepExecution.getWriteCount()
                ,stepExecution.getCommitCount()
        );
        log.info(chunkMessage);
    }

    @AfterChunkError
    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        log.warn("chunkError[{}]", chunkContext);
    }

    @AfterStep
    @Override
    public final ExitStatus afterStep(StepExecution stepExecution) {
        stepStopWatch.stop();
        log.info("-".repeat(80));
        log.info("| [END] StepExecution");
        log.info("| stepName: {}", stepExecution.getStepName());
        log.info("| startTime: {}", Optional.ofNullable(stepExecution.getStartTime()).map(v->DATE_FORMAT.format(v)).orElse(null));
        log.info("| elapsedTime: {}", stepStopWatch.formatTime());
        log.info("| readCount: {}", stepExecution.getReadCount());
        log.info("| filterCount: {}", stepExecution.getFilterCount());
        log.info("| writeCount: {}", stepExecution.getWriteCount());
        log.info("| commitCount: {}", stepExecution.getCommitCount());
        log.info("| rollbackCount: {}", stepExecution.getRollbackCount());
        log.info("| exitStatus: {}", stepExecution.getExitStatus());
        log.info("-".repeat(80));
        return stepExecution.getExitStatus();
    }

}