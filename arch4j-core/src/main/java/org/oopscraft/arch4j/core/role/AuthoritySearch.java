package org.oopscraft.arch4j.core.role;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthoritySearch {

    private String authorityId;

    private String authorityName;

}
