package org.oopscraft.arch4j.core.sample;

import lombok.*;
import org.oopscraft.arch4j.core.sample.entity.SampleEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Sample {

    private String id;

    private String name;

    private SampleType type;

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

    private String cryptoText;

    @Singular("item")
    private List<SampleItem> items = new ArrayList<>();

    public void addItem(SampleItem item) {
        items.add(item);
    }

}