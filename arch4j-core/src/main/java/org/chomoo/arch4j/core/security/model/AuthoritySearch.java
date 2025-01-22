package org.chomoo.arch4j.core.security.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthoritySearch {

    private String authorityId;

    private String name;

}
