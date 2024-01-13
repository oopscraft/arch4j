package org.oopscraft.arch4j.batch.sample.tasklet;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.oopscraft.arch4j.batch.item.file.DelimitedFileItemWriter;
import org.oopscraft.arch4j.batch.item.file.DelimitedFileItemWriterBuilder;
import org.oopscraft.arch4j.batch.item.file.FixedByteFileItemWriter;
import org.oopscraft.arch4j.batch.item.file.GenericFileItemWriter;
import org.oopscraft.arch4j.batch.sample.dto.SampleFile;
import org.oopscraft.arch4j.batch.support.ManualTransactionHandler;
import org.oopscraft.arch4j.core.sample.dao.*;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.ReflectionUtils;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Builder
public class CreateSampleFileTasklet implements Tasklet {

    public static enum FileType {
        DSV, FLD
    }

    @NotNull
    private Integer size;

    @NotNull
    private Resource resource;

    @Builder.Default
    private FileType fileType = FileType.DSV;

    @Builder.Default
    private String lang = "en";

    @Builder.Default
    private String encoding = StandardCharsets.UTF_8.name();

    @Builder.Default
    private String lineSeparator = String.valueOf('\n');

    @Builder.Default
    private String delimiter = String.valueOf('\t');

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        // create sample file
        createSampleFile();

        // returns
        return RepeatStatus.FINISHED;
    }

    private void createSampleFile() {
        FlatFileItemWriter<SampleFile> itemWriter = null;
        switch(fileType) {
            case DSV -> {
                itemWriter = new DelimitedFileItemWriterBuilder<SampleFile>()
                        .resource(resource)
                        .encoding(encoding)
                        .itemType(SampleFile.class)
                        .lineSeparator("\n")
                        .delimiter("\t")
                        .build();
            }
            case FLD -> {
                itemWriter = new FixedByteFileItemWriter<>();
//                itemWriter.setName("itemWriter");
                itemWriter.setResource(resource);
                itemWriter.setEncoding(encoding);
                itemWriter.setLineSeparator(lineSeparator);
            }
            default -> throw new RuntimeException("invalid file type:" + fileType);
        }

        itemWriter.open(new ExecutionContext());
        try {
            for (int i = 0; i < size; i++) {
                Faker faker = new Faker(new Locale(lang), new Random(i));
                SampleFile sample = SampleFile.builder()
                        .sampleId(UUID.randomUUID().toString())
                        .name(faker.name().lastName() + faker.name().firstName())
                        .number(faker.number().numberBetween(-100, 100))
                        .longNumber(faker.number().numberBetween(Long.MIN_VALUE, Long.MAX_VALUE))
                        .doubleNumber(faker.number().randomDouble(4, -100000, 100000))
                        .bigDecimal(BigDecimal.valueOf(faker.number().randomDouble(4, -1234567890, 1234567890)))
                        .sqlDate(new java.sql.Date(System.currentTimeMillis() + faker.number().numberBetween(0, 1234)))
                        .utilDate(new java.util.Date(System.currentTimeMillis() + faker.number().numberBetween(0, 1234)))
                        .timestamp(new java.sql.Timestamp(System.currentTimeMillis() + faker.number().numberBetween(0, 1234)))
                        .localDate(LocalDate.now().plusDays(faker.number().numberBetween(0, 123)))
                        .localDateTime(LocalDateTime.now().withNano(0).plusSeconds(faker.number().numberBetween(0, 1234)))
                        .lobText(faker.address().fullAddress())
                        .cryptoText(faker.business().creditCardNumber())
                        .build();
                itemWriter.write(List.of(sample));
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            itemWriter.close();
        }
    }

}
