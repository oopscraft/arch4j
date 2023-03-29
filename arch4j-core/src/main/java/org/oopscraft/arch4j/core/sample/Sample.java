package org.oopscraft.arch4j.core.sample;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldSupport;
import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "sample")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Sample extends SystemFieldSupport {

    public enum SampleType {
        A, B
    }

    static class SampleTypeConverter extends AbstractEnumConverter<SampleType> {}

    @Id
    @Column(name = "id", length=64)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    @Convert(converter= SampleTypeConverter.class)
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
    private String cryptoText;

}