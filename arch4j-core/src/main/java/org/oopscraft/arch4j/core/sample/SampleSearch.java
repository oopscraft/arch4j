package org.oopscraft.arch4j.core.sample;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class SampleSearch {

    private String id;

    private String name;

    private SampleType type;

}