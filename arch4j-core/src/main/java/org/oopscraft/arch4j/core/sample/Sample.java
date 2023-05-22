package org.oopscraft.arch4j.core.sample;

import lombok.*;
import org.oopscraft.arch4j.core.sample.repository.SampleEntity;
import org.oopscraft.arch4j.core.support.ModelMapperFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Builder.Default
    private List<SampleItem> items = new ArrayList<>();

    public void addItem(SampleItem item) {
        items.add(item);
    }

    /**
     * factory method
     * @param sampleEntity sample entity
     * @return sample
     */
    public static Sample from(SampleEntity sampleEntity) {
        Sample sample = ModelMapperFactory.getInstance()
                .map(sampleEntity, Sample.class);
        sample.setItems(sampleEntity.getItems().stream()
                .map(SampleItem::from)
                .collect(Collectors.toList()));
        return sample;
    }

}