package org.oopscraft.arch4j.core.role;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.role.repository.AuthorityEntity;

/**
 * AuthorityEntity
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority {

    private String authorityId;

    private String name;

    private String note;

    /**
     * factory method
     * @param authorityEntity authority entity
     * @return authority
     */
    public static Authority from(AuthorityEntity authorityEntity) {
        return Authority.builder()
                .authorityId(authorityEntity.getAuthorityId())
                .name(authorityEntity.getName())
                .note(authorityEntity.getNote())
                .build();
    }

}
