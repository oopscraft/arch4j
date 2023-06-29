package org.oopscraft.arch4j.core.page.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "core_page_panel")
@IdClass(PagePanelEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PagePanelEntity extends SystemFieldEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String pageId;
        private Integer index;
    }

    @Id
    @Column(name = "pageId", length = 32)
    private String pageId;

    @Id
    @Column(name = "index")
    private Integer index;

    @Column(name = "type", length = 16)
    private String type;

    @Column(name = "properties")
    @Lob
    private String properties;

}
