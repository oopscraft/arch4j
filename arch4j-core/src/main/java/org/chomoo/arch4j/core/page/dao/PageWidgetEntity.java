package org.chomoo.arch4j.core.page.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity(name = "core_page_widget")
@IdClass(PageWidgetEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageWidgetEntity extends BaseEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String pageId;
        private Integer index;
    }

    @Id
    @Column(name = "page_id", length = 32)
    private String pageId;

    @Id
    @Column(name = "index")
    private Integer index;

    @Column(name = "type", length = 256)
    private String type;

    @Column(name = "properties")
    @Lob
    private String properties;

}
