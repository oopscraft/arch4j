package org.oopscraft.arch4j.core.board.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.oopscraft.arch4j.core.board.MessageFormat;
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
public class BoardEntity extends SystemFieldEntity {

    @Id
    @Column(name = "board_id", length = 64)
    private String boardId;

    @Column(name = "board_name")
    private String boardName;

    @Column(name = "note")
    @Lob
    private String note;

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

    @Column(name = "access_policy", length = 16)
    public SecurityPolicy accessPolicy;

    @Column(name = "read_policy", length = 16)
    public SecurityPolicy readPolicy;

    @Column(name = "write_policy", length = 16)
    public SecurityPolicy writePolicy;

    @Column(name = "comment_enabled", length = 1)
    @Convert(converter= BooleanToYNConverter.class)
    private boolean commentEnabled;

    @Column(name = "comment_policy", length = 16)
    public SecurityPolicy commentPolicy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "board_id",
            referencedColumnName = "board_id",
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @Where(clause = "type = 'READ'")
    @Builder.Default
    private List<BoardRoleEntity> readBoardRoleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "board_id",
            referencedColumnName = "board_id",
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @Where(clause = "type = 'WRITE'")
    @Builder.Default
    private List<BoardRoleEntity> writeBoardRoleEntities = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "board_id",
            referencedColumnName = "board_id",
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    @Where(clause = "type = 'COMMENT'")
    @Builder.Default
    private List<BoardRoleEntity> commentBoardRoleEntities = new ArrayList<>();

}
