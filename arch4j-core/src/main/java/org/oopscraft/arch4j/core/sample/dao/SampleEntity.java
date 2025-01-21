package org.oopscraft.arch4j.core.sample.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.data.converter.CryptoConverter;
import org.oopscraft.arch4j.core.sample.model.SampleType;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_sample")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"items"})
public class SampleEntity extends BaseEntity {

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

    @OneToMany(mappedBy = SampleItemEntity_.SAMPLE_ID, cascade = CascadeType.ALL, orphanRemoval= true, fetch = FetchType.LAZY)
    @OrderBy(SampleItemEntity_.ORDER)
    @Builder.Default
    private List<SampleItemEntity> items = new ArrayList<>();

}