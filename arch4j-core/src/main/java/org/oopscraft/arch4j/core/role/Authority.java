package org.oopscraft.arch4j.core.role;

import lombok.*;
import org.oopscraft.arch4j.core.role.dao.AuthorityEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Authority {

    private String authorityId;

    private String authorityName;

    private String note;

    public static Authority from(AuthorityEntity authorityEntity) {
        return Authority.builder()
                .authorityId(authorityEntity.getAuthorityId())
                .authorityName(authorityEntity.getAuthorityName())
                .note(authorityEntity.getNote())
                .build();
    }

}
