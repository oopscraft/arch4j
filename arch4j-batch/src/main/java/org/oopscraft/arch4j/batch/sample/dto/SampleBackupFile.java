package org.oopscraft.arch4j.batch.sample.dto;

import lombok.*;
import org.oopscraft.arch4j.batch.common.item.file.annotation.Align;
import org.oopscraft.arch4j.batch.common.item.file.annotation.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleBackupFile {

    @Length(20)
    private String sampleId;

    @Length(20)
    private String name;

    @Length(value = 20, align = Align.RIGHT)
    private Integer number;

    @Length(value = 20, align = Align.RIGHT)
    private Long longNumber;

    @Length(value = 20, align = Align.RIGHT)
    private Double doubleNumber;

    @Length(value = 20, align = Align.RIGHT)
    private BigDecimal bigDecimal;

    @Length(20)
    private LocalDateTime localDateTime;

    @Length(20)
    private LocalDate localDate;

    @Length(20)
    private LocalTime localTime;

    @Length(50)
    private String lobText;

    @Length(50)
    private String cryptoText;

}
