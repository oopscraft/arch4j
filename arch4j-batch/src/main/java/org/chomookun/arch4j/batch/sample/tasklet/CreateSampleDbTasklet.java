package org.chomookun.arch4j.batch.sample.tasklet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.chomookun.arch4j.batch.common.support.ManualTransactionHandler;
import org.chomookun.arch4j.core.sample.model.SampleType;
import org.chomookun.arch4j.core.sample.dao.QSampleBackupEntity;
import org.chomookun.arch4j.core.sample.dao.QSampleEntity;
import org.chomookun.arch4j.core.sample.dao.SampleEntity;
import org.chomookun.arch4j.core.sample.dao.SampleRepository;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Random;

@Slf4j
@Builder
public class CreateSampleDbTasklet implements Tasklet {

    private Integer size;

    @Builder.Default
    private String lang = "en";

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Autowired
    SampleRepository sampleRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {

        // clear sample
        clearSampleDb();

        // clear sampleBackup
        clearSampleBackupDb();

        // create sample
        createSampleDb();

        // returns
        return RepeatStatus.FINISHED;
    }

    private void clearSampleDb() {
        try (ManualTransactionHandler manualTransactionHandler = new ManualTransactionHandler(transactionManager)) {
            jpaQueryFactory.delete(QSampleEntity.sampleEntity).execute();
            manualTransactionHandler.commit();
        }
    }

    private void clearSampleBackupDb() {
        try (ManualTransactionHandler manualTransactionHandler = new ManualTransactionHandler(transactionManager)) {
            jpaQueryFactory.delete(QSampleBackupEntity.sampleBackupEntity).execute();
            manualTransactionHandler.commit();
        }
    }

    private void createSampleDb() {
        try (ManualTransactionHandler manualTransactionUtil = new ManualTransactionHandler(transactionManager)) {
            for (int i = 0; i < size; i++) {
                Faker faker = new Faker(new Locale(lang), new Random(i));
                SampleEntity sample = SampleEntity.builder()
                        .sampleId(String.valueOf(i))
                        .sampleName(faker.name().fullName())
                        .sampleType(SampleType.A)
                        .number(faker.number().numberBetween(-123, 123))
                        .longNumber(faker.number().numberBetween(-12345L, 12345L))
                        .doubleNumber(faker.number().randomDouble(2, -12345, 12345))
                        .bigDecimal(BigDecimal.valueOf(faker.number().randomDouble(2, -12345, 12345)))
                        .localDateTime(LocalDateTime.now().withNano(0).plusSeconds(faker.number().numberBetween(0, 1234)))
                        .localDate(LocalDate.now().plusDays(faker.number().numberBetween(0, 123)))
                        .localTime(LocalTime.now().plusSeconds(faker.number().numberBetween(0,60)))
                        .lobText(faker.address().cityName() + " " + faker.address().streetName())
                        .cryptoText(faker.business().creditCardNumber())
                        .build();
                sampleRepository.saveAndFlush(sample);

                // interval commit
                if (i % 100 == 0) {
                    manualTransactionUtil.commit();
                }
            }

            // final flush commit
            manualTransactionUtil.commit();
        }

        // check create count
        long totalCount = sampleRepository.count();
        if(totalCount != this.size) {
            throw new RuntimeException(String.format("Create SampleEntity count mismatch - size:%d, totalCount:%d", size, totalCount));
        }
    }

}
