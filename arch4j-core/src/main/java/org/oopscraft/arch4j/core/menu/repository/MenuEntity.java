package org.oopscraft.arch4j.core.menu.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

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

    @Column(name = "name")
    private String name;

    @Column(name = "link")
    private String link;

    @Column(name = "target")
    private String target;

    @Column(name = "sort")
    private Integer sort;

    @OneToMany(mappedBy = MenuI18nEntity_.ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MenuI18nEntity> i18n = new ArrayList<>();

}
