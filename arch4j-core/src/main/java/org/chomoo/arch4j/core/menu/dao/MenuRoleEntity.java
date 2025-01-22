package org.chomoo.arch4j.core.menu.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseEntity;
import org.chomoo.arch4j.core.security.dao.RoleEntity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_menu_role")
@IdClass(MenuRoleEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuRoleEntity extends BaseEntity {

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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "menu_id",
            insertable = false,
            updatable = false
    )
    private MenuEntity menuEntity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(
            name = "role_id",
            insertable = false,
            updatable = false
    )
    private RoleEntity roleEntity;

}
