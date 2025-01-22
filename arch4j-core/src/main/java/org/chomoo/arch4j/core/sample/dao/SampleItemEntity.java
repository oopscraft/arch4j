package org.chomoo.arch4j.core.sample.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(SampleItemEntity.Pk.class)
@Table(name = "core_sample_item")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SampleItemEntity extends BaseEntity {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        private String sampleId;
        private String itemId;
    }

    @Id
    @Column(name = "sample_id", length = 64)
    private String sampleId;

    @Id
    @Column(name = "item_id", length = 64)
    private String itemId;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "value")
    private String value;

    @Column(name = "order")
    private Integer order;

}
