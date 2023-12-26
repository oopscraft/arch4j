package org.oopscraft.arch4j.core.board.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.oopscraft.arch4j.core.board.MessageFormat;
import org.oopscraft.arch4j.core.data.i18n.I18nGetter;
import org.oopscraft.arch4j.core.data.i18n.I18nSetter;
import org.oopscraft.arch4j.core.data.i18n.I18nSupportEntity;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.converter.BooleanToYNConverter;

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
public class BoardEntity extends SystemFieldEntity implements I18nSupportEntity<BoardI18nEntity> {

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

    @Column(name = "file_enabled", length = 1)
    @Convert(converter= BooleanToYNConverter.class)
    private boolean fileEnabled;

    @Column(name = "file_size_limit")
    private Integer fileSizeLimit;

    @Column(name = "comment_enabled", length = 1)
    @Convert(converter= BooleanToYNConverter.class)
    private boolean commentEnabled;

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
    private List<BoardI18nEntity> i18ns = new ArrayList<>();

    @Override
    public List<BoardI18nEntity> provideI18nEntities() {
        return this.i18ns;
    }

    @Override
    public BoardI18nEntity provideNewI18nEntity(String language) {
        return BoardI18nEntity.builder()
                .boardId(this.boardId)
                .language(language)
                .build();
    }

    public void setBoardName(String boardName) {
        I18nSetter.of(this, this.boardName)
                .whenDefault(() -> this.boardName = boardName)
                .whenI18n(i18nEntity -> i18nEntity.setBoardName(boardName))
                .set();
    }

    public String getBoardName() {
        return I18nGetter.of(this, this.boardName)
                .whenDefault(() -> this.boardName)
                .whenI18n(BoardI18nEntity::getBoardName)
                .get();
    }

    public void setMessage(String message) {
        I18nSetter.of(this, this.message)
                .whenDefault(() -> this.message = message)
                .whenI18n(i18nEntity -> i18nEntity.setMessage(message))
                .set();
    }

    public String getMessage() {
        return I18nGetter.of(this, this.message)
                .whenDefault(() -> this.message)
                .whenI18n(BoardI18nEntity::getMessage)
                .get();
    }

}
