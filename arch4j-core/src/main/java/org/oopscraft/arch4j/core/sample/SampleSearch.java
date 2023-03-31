package org.oopscraft.arch4j.core.sample;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SampleSearch {

    private String id;

    private String name;

    private SampleType type;

}