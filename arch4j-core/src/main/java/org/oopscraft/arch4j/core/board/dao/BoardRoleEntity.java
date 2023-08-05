package org.oopscraft.arch4j.core.board.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_board_role")
@IdClass(BoardRoleEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardRoleEntity extends SystemFieldEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String boardId;
        private String roleId;
        private String type;
    }

    @Id
    @Column(name = "board_id")
    private String boardId;

    @Id
    @Column(name = "role_id")
    private String roleId;

    @Id
    @Column(name = "type", length = 16)
    private String type;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "role_id",
            referencedColumnName = "role_id",
            insertable = false,
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
    private RoleEntity roleEntity;


}
