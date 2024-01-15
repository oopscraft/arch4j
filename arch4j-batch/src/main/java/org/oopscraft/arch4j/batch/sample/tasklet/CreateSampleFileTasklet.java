package org.oopscraft.arch4j.batch.sample.tasklet;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.oopscraft.arch4j.batch.item.file.DelimiterFileItemWriterBuilder;
import org.oopscraft.arch4j.batch.item.file.FixedByteFileItemWriterBuilder;
import org.oopscraft.arch4j.batch.sample.dto.SampleFile;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Builder
public class CreateSampleFileTasklet implements Tasklet {

    public static enum FileType {
        DSV, FLD
    }

    @NotNull
    private Integer size;

    @Builder.Default
    private String lang = "en";

    @NotNull
    private Resource resource;

    @Builder.Default
    private FileType fileType = FileType.DSV;

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
                itemWriter = new DelimiterFileItemWriterBuilder<SampleFile>()
                        .itemType(SampleFile.class)
                        .resource(resource)
                        .encoding(encoding)
                        .lineSeparator(lineSeparator)
                        .delimiter(delimiter)
                        .build();
            }
            case FLD -> {
                itemWriter = new FixedByteFileItemWriterBuilder<SampleFile>()
                        .itemType(SampleFile.class)
                        .resource(resource)
                        .encoding(encoding)
                        .lineSeparator(lineSeparator)
                        .build();
            }
            default -> throw new RuntimeException("invalid file type:" + fileType);
        }

        itemWriter.open(new ExecutionContext());
        try {
            for (int i = 0; i < size; i++) {
                Faker faker = new Faker(new Locale(lang), new Random(i));
                SampleFile sample = SampleFile.builder()
                        .sampleId(String.valueOf(i))
                        .name(faker.name().fullName())
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
                itemWriter.write(List.of(sample));
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally{
            itemWriter.close();
        }
    }

}
