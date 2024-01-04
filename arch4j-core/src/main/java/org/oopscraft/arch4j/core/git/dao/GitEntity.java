package org.oopscraft.arch4j.core.git.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

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

    @Column(name = "git_name")
    private String gitName;

    @Column(name = "note")
    private String note;

    @Column(name = "url")
    private String url;

    @Column(name = "branch", length = 32)
    private String branch;

}
