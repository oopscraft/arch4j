package org.oopscraft.arch4j.core.page.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.language.LanguageGetter;
import org.oopscraft.arch4j.core.data.language.LanguageSetter;
import org.oopscraft.arch4j.core.data.language.LanguageSupportEntity;
import org.oopscraft.arch4j.core.page.ContentFormat;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "core_page")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageEntity extends SystemFieldEntity implements LanguageSupportEntity<PageLanguageEntity> {

    @Id
    @Column(name = "page_id", length = 32)
    private String pageId;

    @Column(name = "page_name")
    private String pageName;

    @Column(name = "content_format", length = 16)
    private ContentFormat contentFormat;

    @Column(name = "content")
    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "page_id", updatable = false)
    @OrderBy(PageWidgetEntity_.INDEX)
    @Builder.Default
    private List<PageWidgetEntity> pageWidgetEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "page_id", updatable = false)
    @Builder.Default
    private List<PageLanguageEntity> pageLanguageEntities = new ArrayList<>();

    @Override
    public List<PageLanguageEntity> provideLanguageEntities() {
        return this.pageLanguageEntities;
    }

    @Override
    public PageLanguageEntity provideNewLanguageEntity(String language) {
        return PageLanguageEntity.builder()
                .pageId(pageId)
                .language(language)
                .build();
    }

    public void setContent(String content) {
        LanguageSetter.of(this, this.content)
                .defaultSet(() -> this.content = content)
                .languageSet(pageLanguageEntity -> pageLanguageEntity.setContent(content))
                .set();
    }

    public String getContent() {
        return LanguageGetter.of(this, this.content)
                .defaultGet(() -> this.content)
                .languageGet(PageLanguageEntity::getContent)
                .get();
    }

}
