package org.chomoo.arch4j.web.sample.api.v1.dto;

import lombok.*;
import org.chomoo.arch4j.core.sample.model.SampleType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class SampleResponse {

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
    private List<Item> items = new ArrayList<>();

    public void addItem(Item item) {
        items.add(item);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PACKAGE)
    public static class Item {

        private String id;

        private String name;

        private String value;

    }

}
