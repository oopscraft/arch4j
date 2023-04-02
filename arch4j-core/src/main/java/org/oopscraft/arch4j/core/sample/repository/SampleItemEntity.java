package org.oopscraft.arch4j.core.sample.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@IdClass(SampleItemEntity.Pk.class)
@Table(name = "sample_item")
@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SampleItemEntity extends SystemFieldEntity {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        private String sampleId;
        private String id;
    }

    @Id
    @Column(name = "sample_id", length = 64)
    private String sampleId;

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;

    @Column(name = "order")
    private Integer order;

}
