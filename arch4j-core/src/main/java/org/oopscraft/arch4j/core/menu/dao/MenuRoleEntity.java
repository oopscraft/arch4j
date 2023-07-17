package org.oopscraft.arch4j.core.menu.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_menu_role")
@IdClass(MenuRoleEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRoleEntity extends SystemFieldEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String menuId;
        private String roleId;
        private String type;
    }

    @Id
    @Column(name = "menu_id")
    private String menuId;

    @Id
    @Column(name = "role_id")
    private String roleId;

    @Id
    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", foreignKey = @ForeignKey(name = "none"), insertable = false, updatable = false)
    private RoleEntity role;

}
