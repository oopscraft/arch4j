package org.oopscraft.arch4j.core.page.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
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
public class PageEntity extends SystemFieldEntity {

    @Id
    @Column(name = "pageId", length = 32)
    private String pageId;

    @Column(name = "name")
    private String name;

    @Column(name = "content_format", length = 16)
    private ContentFormat contentFormat;

    @Column(name = "content")
    @Lob
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = PageEntity_.PAGE_ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(PageWidgetEntity_.INDEX)
    @Builder.Default
    private List<PageWidgetEntity> widgets = new ArrayList<>();

}
