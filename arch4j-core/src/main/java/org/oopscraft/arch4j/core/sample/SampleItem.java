package org.oopscraft.arch4j.core.sample;

import lombok.*;
import org.oopscraft.arch4j.core.sample.repository.SampleItemEntity;
import org.oopscraft.arch4j.core.support.ModelMapperFactory;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleItem {

    private String sampleId;

    private String id;

    private String name;

    private String value;

    private Integer order;

    /**
     * factory method
     * @param sampleItemEntity sample item entity
     * @return sample item
     */
    public static SampleItem from(SampleItemEntity sampleItemEntity) {
        return ModelMapperFactory.getInstance()
                .map(sampleItemEntity, SampleItem.class);
    }

}