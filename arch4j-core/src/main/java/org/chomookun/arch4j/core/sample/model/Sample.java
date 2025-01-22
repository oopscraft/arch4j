package org.chomookun.arch4j.core.sample.model;

import lombok.*;
import org.chomookun.arch4j.core.sample.dao.SampleEntity;
import org.chomookun.arch4j.core.common.support.ModelMapperFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Sample {

    private String sampleId;

    private String sampleName;

    private SampleType sampleType;

    private Integer number;

    private Long longNumber;

    private Double doubleNumber;

    private BigDecimal bigDecimal;

    private LocalDateTime localDateTime;

    private LocalDate localDate;

    private LocalTime localTime;

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