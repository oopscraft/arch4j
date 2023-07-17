package org.oopscraft.arch4j.core.menu.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.menu.MenuTarget;
import org.oopscraft.arch4j.core.security.SecurityPolicy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "core_menu",
    indexes = {
        @Index(name = "ix_parent_menu_id", columnList = "parent_menu_id")
    }
)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuEntity extends SystemFieldEntity {

    @Id
    @Column(name = "menu_id", length = 32)
    private String menuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "parent_menu_id", length = 32)
    private String parentMenuId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private MenuEntity parentMenu;

    @Column(name = "link")
    private String link;

    @Column(name = "target", length = 8)
    private MenuTarget target;

    @Column(name = "icon")
    @Lob
    private String icon;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "note")
    @Lob
    private String note;

    @Column(name = "view_policy", length = 16)
    public SecurityPolicy viewPolicy;

    @Column(name = "link_policy", length = 16)
    public SecurityPolicy linkPolicy;

    @OneToMany(mappedBy = MenuRoleEntity_.MENU_ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "type = 'VIEW'")
    @Builder.Default
    private List<MenuRoleEntity> viewRoles = new ArrayList<>();

    @OneToMany(mappedBy = MenuRoleEntity_.MENU_ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "type = 'LINK'")
    @Builder.Default
    private List<MenuRoleEntity> linkRoles = new ArrayList<>();

    @OneToMany(mappedBy = MenuI18nEntity_.MENU_ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MenuI18nEntity> i18ns = new ArrayList<>();

}
