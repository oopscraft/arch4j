package org.oopscraft.arch4j.core.board.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;

    @Column(name = "icon")
    private String icon;

    @Column(name = "skin")
    private String skin;

    @Column(name = "page_size")
    private Integer pageSize;

    @Column(name = "reply_enabled")
    private boolean replyEnabled;

    @Column(name = "file_enabled")
    private boolean fileEnabled;

    /**
     * board access roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "board_role_access",
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
            name = "board_role_read",
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
            name = "board_role_write",
            joinColumns = @JoinColumn(name = "board_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    private List<RoleEntity> writeRoles = new ArrayList<>();


}
