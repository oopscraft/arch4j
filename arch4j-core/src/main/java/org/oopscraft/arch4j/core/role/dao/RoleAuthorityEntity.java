package org.oopscraft.arch4j.core.role.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemEntity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "core_role_authority")
@IdClass(RoleAuthorityEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleAuthorityEntity extends SystemEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String roleId;
        private String authorityId;
    }

    @Id
    @Column(name = "role_id")
    private String roleId;

    @Id
    @Column(name = "authority_id")
    private String authorityId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private RoleEntity roleEntity;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "authority_id", insertable = false, updatable = false)
    private AuthorityEntity authorityEntity;

}
