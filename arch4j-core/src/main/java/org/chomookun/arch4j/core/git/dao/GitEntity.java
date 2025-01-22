package org.chomookun.arch4j.core.git.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;

import jakarta.persistence.*;

@Entity(name = "core_git")
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GitEntity extends BaseEntity {

    @Id
    @Column(name = "git_id", length = 32)
    private String gitId;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "branch", length = 32)
    private String branch;

    @Column(name = "note")
    private String note;

}
