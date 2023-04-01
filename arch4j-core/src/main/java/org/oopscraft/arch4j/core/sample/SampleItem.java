package org.oopscraft.arch4j.core.sample;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.converter.CryptoConverter;
import org.oopscraft.arch4j.core.sample.entity.SampleItemEntity;
import org.oopscraft.arch4j.core.sample.entity.SampleItemEntity_;
import org.oopscraft.arch4j.core.support.ModelMapperFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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