package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_article")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    @Lob
    private String content;

    @Column(name = "boardId")
    private String boardId;

    @Column(name = "userId")
    private String userId;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = ArticleEntity_.USER_ID, insertable = false, updatable = false)
    private UserEntity user;

    @OneToMany(
            mappedBy = ArticleFileEntity_.ARTICLE_ID,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<ArticleFileEntity> files = new ArrayList<>();

}
