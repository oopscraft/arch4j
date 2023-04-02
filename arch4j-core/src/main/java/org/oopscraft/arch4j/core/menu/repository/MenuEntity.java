package org.oopscraft.arch4j.core.menu.repository;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class MenuEntity {

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

}
