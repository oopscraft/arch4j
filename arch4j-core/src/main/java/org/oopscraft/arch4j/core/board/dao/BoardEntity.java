package org.oopscraft.arch4j.core.board.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.oopscraft.arch4j.core.board.MessageFormat;
import org.oopscraft.arch4j.core.data.language.LanguageGetter;
import org.oopscraft.arch4j.core.data.language.LanguageSetter;
import org.oopscraft.arch4j.core.data.language.LanguageSupportEntity;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.converter.BooleanToYNConverter;
import org.oopscraft.arch4j.core.security.SecurityPolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_board")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardEntity extends SystemFieldEntity implements LanguageSupportEntity<BoardLanguageEntity> {

    @Id
    @Column(name = "board_id", length = 64)
    private String boardId;

    @Column(name = "board_name")
    private String boardName;

    @Column(name = "icon")
    @Lob
    private String icon;

    @Column(name = "message_format", length = 16)
    private MessageFormat messageFormat;

    @Column(name = "message")
    @Lob
    private String message;

    @Column(name = "skin")
    private String skin;

    @Column(name = "page_size")
    private Integer pageSize;

    @Column(name = "access_policy", length = 16)
    public SecurityPolicy accessPolicy;

    @Column(name = "read_policy", length = 16)
    public SecurityPolicy readPolicy;

    @Column(name = "write_policy", length = 16)
    public SecurityPolicy writePolicy;

    @Column(name = "file_enabled", length = 1)
    @Convert(converter= BooleanToYNConverter.class)
    private boolean fileEnabled;

    @Column(name = "file_size_limit")
    private Integer fileSizeLimit;

    @Column(name = "file_policy", length = 16)
    private SecurityPolicy filePolicy;

    @Column(name = "comment_enabled", length = 1)
    @Convert(converter= BooleanToYNConverter.class)
    private boolean commentEnabled;

    @Column(name = "comment_policy", length = 16)
    public SecurityPolicy commentPolicy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "type = 'ACCESS'")
    @Builder.Default
    private List<BoardRoleEntity> accessBoardRoleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "type = 'READ'")
    @Builder.Default
    private List<BoardRoleEntity> readBoardRoleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "type = 'WRITE'")
    @Builder.Default
    private List<BoardRoleEntity> writeBoardRoleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "type = 'FILE'")
    @Builder.Default
    private List<BoardRoleEntity> fileBoardRoleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @Where(clause = "type = 'COMMENT'")
    @Builder.Default
    private List<BoardRoleEntity> commentBoardRoleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "board_id", updatable = false)
    @Builder.Default
    private List<BoardLanguageEntity> boardLanguageEntities = new ArrayList<>();

    @Override
    public List<BoardLanguageEntity> provideLanguageEntities() {
        return this.boardLanguageEntities;
    }

    @Override
    public BoardLanguageEntity provideNewLanguageEntity(String language) {
        return BoardLanguageEntity.builder()
                .boardId(this.boardId)
                .language(language)
                .build();
    }

    public void setBoardName(String boardName) {
        LanguageSetter.of(this, this.boardName)
                .defaultSet(() -> this.boardName = boardName)
                .languageSet(boardLanguageEntity -> boardLanguageEntity.setBoardName(boardName))
                .set();
    }

    public String getBoardName() {
        return LanguageGetter.of(this, this.boardName)
                .defaultGet(() -> this.boardName)
                .languageGet(BoardLanguageEntity::getBoardName)
                .get();
    }

    public void setMessage(String message) {
        LanguageSetter.of(this, this.message)
                .defaultSet(() -> this.message = message)
                .languageSet(boardLanguageEntity -> boardLanguageEntity.setMessage(message))
                .set();
    }

    public String getMessage() {
        return LanguageGetter.of(this, this.message)
                .defaultGet(() -> this.message)
                .languageGet(BoardLanguageEntity::getMessage)
                .get();
    }

}
