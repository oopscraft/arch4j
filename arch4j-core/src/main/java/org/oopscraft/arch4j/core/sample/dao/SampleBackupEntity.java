package org.oopscraft.arch4j.core.sample.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.data.converter.CryptoConverter;
import org.oopscraft.arch4j.core.sample.model.SampleType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "core_sample_backup")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SampleBackupEntity extends BaseEntity {

    @Id
    @Column(name = "sample_id", length=64)
    private String sampleId;

    @Column(name = "sample_name", nullable = false)
    private String sampleName;

    @Column(name = "sample_type")
    private SampleType sampleType;

    @Column(name = "number")
    private Integer number;

    @Column(name = "long_number")
    private Long longNumber;

    @Column(name = "double_number")
    private Double doubleNumber;

    @Column(name = "big_decimal")
    private BigDecimal bigDecimal;

    @Column(name = "local_date_time")
    private LocalDateTime localDateTime;

    @Column(name = "local_date")
    private LocalDate localDate;

    @Column(name = "local_time")
    private LocalTime localTime;

    @Column(name = "lob_text")
    @Lob
    private String lobText;

    @Column(name = "crypto_text")
    @Lob
    @Convert(converter = CryptoConverter.class)
    private String cryptoText;

}