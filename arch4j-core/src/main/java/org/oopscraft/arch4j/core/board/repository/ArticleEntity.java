package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.file.FileInfo;
import org.oopscraft.arch4j.core.file.repository.FileInfoEntity;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;
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

    @Column(name = "comment_count")
    @Builder.Default
    private Long commentCount = 0L;

    @Column(name = "like_count")
    @Builder.Default
    private Long likeCount = 0L;

    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = ArticleEntity_.USER_ID, insertable = false, updatable = false)
    private UserEntity user;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "core_article_file",
            joinColumns = @JoinColumn(name = "article_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "file_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    List<FileInfoEntity> files = new ArrayList<>();

}
