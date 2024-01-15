package org.oopscraft.arch4j.core.sample;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SampleSearch {

    private String sampleId;

    private String sampleName;

    private SampleType sampleType;

}