package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_article_vote")
@IdClass(ArticleVoteEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleVoteEntity extends SystemFieldEntity {

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        private String articleId;
        private String userId;
    }

    @Id
    @Column(name = "article_id", length = 32)
    private String articleId;

    @Id
    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "point")
    @Builder.Default
    private Long point = 0L;

}
