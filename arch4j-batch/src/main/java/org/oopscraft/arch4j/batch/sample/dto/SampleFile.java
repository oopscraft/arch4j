package org.oopscraft.arch4j.batch.sample.dto;

import lombok.*;
import org.oopscraft.arch4j.core.data.crpyto.Crypto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleFile {

    private String sampleId;

    private String name;

    private String type;

    private Integer number;

    private Long longNumber;

    private Double doubleNumber;

    private BigDecimal bigDecimal;

    private java.sql.Date sqlDate;

    private java.util.Date utilDate;

    private java.sql.Timestamp timestamp;

    private LocalDate localDate;

    private LocalDateTime localDateTime;

    private String lobText;

    @Crypto
    private String cryptoText;

}
