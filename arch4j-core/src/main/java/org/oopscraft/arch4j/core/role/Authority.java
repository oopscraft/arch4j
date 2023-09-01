package org.oopscraft.arch4j.core.role;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldModel;
import org.oopscraft.arch4j.core.role.dao.AuthorityEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority extends SystemFieldModel {

    private String authorityId;

    private String authorityName;

    private String note;

    public static Authority from(AuthorityEntity authorityEntity) {
        return Authority.builder()
                .systemRequired(authorityEntity.isSystemRequired())
                .systemUpdatedAt(authorityEntity.getSystemUpdatedAt())
                .systemUpdatedBy(authorityEntity.getSystemUpdatedBy())
                .authorityId(authorityEntity.getAuthorityId())
                .authorityName(authorityEntity.getAuthorityName())
                .note(authorityEntity.getNote())
                .build();
    }

}
