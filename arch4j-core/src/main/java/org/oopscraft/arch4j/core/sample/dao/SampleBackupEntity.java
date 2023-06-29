package org.oopscraft.arch4j.core.sample.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.converter.CryptoConverter;
import org.oopscraft.arch4j.core.sample.SampleType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "core_sample_backup")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SampleBackupEntity extends SystemFieldEntity {

    @Id
    @Column(name = "sample_id", length=64)
    private String sampleId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private SampleType type;

    @Column(name = "number")
    private Integer number;

    @Column(name = "long_number")
    private Long longNumber;

    @Column(name = "double_number")
    private Double doubleNumber;

    @Column(name = "big_decimal")
    private BigDecimal bigDecimal;

    @Column(name = "sql_date")
    private java.sql.Date sqlDate;

    @Column(name = "util_date")
    private java.util.Date utilDate;

    @Column(name = "timestamp")
    private java.sql.Timestamp timestamp;

    @Column(name = "local_date")
    private LocalDate localDate;

    @Column(name = "local_date_time")
    private LocalDateTime localDateTime;

    @Column(name = "lob_text")
    @Lob
    private String lobText;

    @Column(name = "crypto_text")
    @Lob
    @Convert(converter = CryptoConverter.class)
    private String cryptoText;

}