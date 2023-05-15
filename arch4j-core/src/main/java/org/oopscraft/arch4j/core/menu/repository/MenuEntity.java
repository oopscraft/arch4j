package org.oopscraft.arch4j.core.menu.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.user.repository.AuthorityEntity;
import org.oopscraft.arch4j.core.user.repository.RoleEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menu")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "parent_id")
    private String parentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    private MenuEntity parentMenu;

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "target")
    private String target;

    @Column(name = "icon")
    @Lob
    private String icon;

    @Column(name = "sort")
    private Integer sort;

    @Column(name = "note")
    private String note;

    @ManyToMany
    @JoinTable(
            name = "menu_role",
            joinColumns = @JoinColumn(name = "menu_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    List<RoleEntity> roles = new ArrayList<>();

    @OneToMany(mappedBy = MenuI18nEntity_.ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MenuI18nEntity> i18n = new ArrayList<>();

}
