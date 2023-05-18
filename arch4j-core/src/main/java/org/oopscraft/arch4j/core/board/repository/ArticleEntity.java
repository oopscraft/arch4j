package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "article")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "boardId")
    private String boardId;

    @Column(name = "userId")
    private String userId;

    @OneToMany(
            mappedBy = ArticleFileEntity_.ARTICLE_ID,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ArticleFileEntity> files = new ArrayList<>();

    @OneToMany(
            mappedBy = ArticleReplyEntity_.ARTICLE_ID,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ArticleReplyEntity> replies = new ArrayList<>();

}
