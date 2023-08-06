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
    @JoinColumn(
            name = "page_id",
            referencedColumnName = "page_id",
            updatable = false
    )
    @OrderBy(PageWidgetEntity_.INDEX)
    @Builder.Default
    private List<PageWidgetEntity> pageWidgetEntities = new ArrayList<>();

}
