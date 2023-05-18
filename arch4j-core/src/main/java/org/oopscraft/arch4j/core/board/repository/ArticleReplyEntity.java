package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "article_reply")
@IdClass(ArticleReplyEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleReplyEntity extends SystemFieldEntity {

    @Data
    public static class Pk implements Serializable {

        private String articleId;

        private String id;
    }

    @Id
    @Column(name = "article_id")
    private String articleId;

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "parent_id")
    private String parentId;

    @Column(name = "comment")
    private String comment;

    @Column(name = "userId")
    private String userId;


}
