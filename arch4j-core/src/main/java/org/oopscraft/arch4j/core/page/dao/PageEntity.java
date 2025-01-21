package org.oopscraft.arch4j.core.page.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.i18n.I18nGetter;
import org.oopscraft.arch4j.core.common.i18n.I18nSetter;
import org.oopscraft.arch4j.core.common.i18n.I18nSupportEntity;
import org.oopscraft.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.page.model.Page;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "core_page")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageEntity extends BaseEntity implements I18nSupportEntity<PageI18nEntity> {

    @Id
    @Column(name = "page_id", length = 32)
    private String pageId;

    @Column(name = "name")
    private String name;

    @Column(name = "content_format", length = 16)
    private Page.ContentFormat contentFormat;

    @Column(name = "content")
    @Lob
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "page_id", updatable = false)
    @OrderBy(PageWidgetEntity_.INDEX)
    @Builder.Default
    private List<PageWidgetEntity> pageWidgets = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "page_id", updatable = false)
    @Builder.Default
    private List<PageI18nEntity> pageI18ns = new ArrayList<>();

    @Override
    public List<PageI18nEntity> provideI18nEntities() {
        return this.pageI18ns;
    }

    @Override
    public PageI18nEntity provideNewI18nEntity(String language) {
        return PageI18nEntity.builder()
                .pageId(pageId)
                .language(language)
                .build();
    }

    public void setContent(String content) {
        I18nSetter.of(this, this.content)
                .whenDefault(() -> this.content = content)
                .whenI18n(pageLanguageEntity -> pageLanguageEntity.setContent(content))
                .set();
    }

    public String getContent() {
        return I18nGetter.of(this, this.content)
                .whenDefault(() -> this.content)
                .whenI18n(PageI18nEntity::getContent)
                .get();
    }

    @Converter(autoApply = true)
    public static class ContentFormatConverter extends AbstractEnumConverter<Page.ContentFormat> {}

}
