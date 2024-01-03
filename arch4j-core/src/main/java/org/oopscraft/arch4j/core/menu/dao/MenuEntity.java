package org.oopscraft.arch4j.core.menu.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import org.oopscraft.arch4j.core.data.i18n.I18nGetter;
import org.oopscraft.arch4j.core.data.i18n.I18nSetter;
import org.oopscraft.arch4j.core.data.i18n.I18nSupportEntity;
import org.oopscraft.arch4j.core.data.SystemEntity;
import org.oopscraft.arch4j.core.menu.MenuTarget;

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
public class MenuEntity extends SystemEntity implements I18nSupportEntity<MenuI18nEntity> {

    @Id
    @Column(name = "menu_id", length = 32)
    private String menuId;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "parent_menu_id", length = 32)
    private String parentMenuId;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_menu_id", referencedColumnName = "menu_id", insertable = false, updatable = false)
    private MenuEntity parentMenu;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", updatable = false)
    @Where(clause = "type = 'VIEW'")
    @Builder.Default
    private List<MenuRoleEntity> viewMenuRoles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", updatable = false)
    @Where(clause = "type = 'LINK'")
    @Builder.Default
    private List<MenuRoleEntity> linkMenuRoles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", updatable = false)
    @Builder.Default
    private List<MenuI18nEntity> menuI18ns = new ArrayList<>();

    @Override
    public List<MenuI18nEntity> provideI18nEntities() {
        return this.menuI18ns;
    }

    @Override
    public MenuI18nEntity provideNewI18nEntity(String language) {
        return MenuI18nEntity.builder()
                .menuId(menuId)
                .language(language)
                .build();
    }

    public void setMenuName(String menuName) {
        I18nSetter.of(this, this.menuName)
                .whenDefault(() -> this.menuName = menuName)
                .whenI18n(menuLanguageEntity -> menuLanguageEntity.setMenuName(menuName))
                .set();
    }

    public String getMenuName() {
        return I18nGetter.of(this, this.menuName)
                .whenDefault(() -> this.menuName)
                .whenI18n(MenuI18nEntity::getMenuName)
                .get();
    }

}
