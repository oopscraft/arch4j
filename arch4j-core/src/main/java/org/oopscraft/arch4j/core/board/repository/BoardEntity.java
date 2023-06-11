package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;

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

    @Column(name = "name")
    private String name;

    @Column(name = "icon")
    @Lob
    private String icon;

    @Column(name = "note")
    private String note;

    @Column(name = "skin")
    private String skin;

    @Column(name = "page_size")
    private Integer pageSize;

    @Column(name = "file_enabled")
    private boolean fileEnabled;

    @Column(name = "comment_enabled")
    private boolean commentEnabled;

    /**
     * board access roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "core_board_role_access",
            joinColumns = @JoinColumn(name = "board_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    private List<RoleEntity> accessRoles = new ArrayList<>();

    /**
     * board read roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "core_board_role_read",
            joinColumns = @JoinColumn(name = "board_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    private List<RoleEntity> readRoles = new ArrayList<>();

    /**
     * board write roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "core_board_role_write",
            joinColumns = @JoinColumn(name = "board_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    private List<RoleEntity> writeRoles = new ArrayList<>();


}
