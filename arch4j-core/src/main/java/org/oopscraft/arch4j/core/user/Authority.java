package org.oopscraft.arch4j.core.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.user.repository.AuthorityEntity;

import javax.validation.constraints.NotBlank;

/**
 * AuthorityEntity
 */
@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends SystemFieldEntity {

    private String id;

    private String name;
    
    private String icon;
    
    private String note;

    /**
     * factory method
     * @param authorityEntity authority entity
     * @return authority
     */
    public static Authority from(AuthorityEntity authorityEntity) {
        return Authority.builder()
                .id(authorityEntity.getId())
                .name(authorityEntity.getName())
                .icon(authorityEntity.getIcon())
                .note(authorityEntity.getNote())
                .build();
    }

}
