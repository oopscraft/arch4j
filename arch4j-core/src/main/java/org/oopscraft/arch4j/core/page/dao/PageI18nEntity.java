package org.oopscraft.arch4j.core.page.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.i18n.I18nEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_page_i18n")
@IdClass(PageI18nEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageI18nEntity extends BaseEntity implements I18nEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String pageId;
        private String language;
    }

    @Id
    @Column(name = "page_id", length = 32)
    private String pageId;

    @Id
    @Column(name = "language", length = 8)
    private String language;

    @Column(name = "content")
    @Lob
    private String content;

}

