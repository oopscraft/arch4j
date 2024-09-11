package org.oopscraft.arch4j.core.security.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseModel;
import org.oopscraft.arch4j.core.security.dao.AuthorityEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority extends BaseModel {

    private String authorityId;

    private String name;

    private String note;

    /**
     * authority factory method
     * @param authorityEntity authority entity
     * @return authority
     */
    public static Authority from(AuthorityEntity authorityEntity) {
        return Authority.builder()
                .systemRequired(authorityEntity.isSystemRequired())
                .systemUpdatedAt(authorityEntity.getSystemUpdatedAt())
                .systemUpdatedBy(authorityEntity.getSystemUpdatedBy())
                .authorityId(authorityEntity.getAuthorityId())
                .name(authorityEntity.getName())
                .note(authorityEntity.getNote())
                .build();
    }

}
