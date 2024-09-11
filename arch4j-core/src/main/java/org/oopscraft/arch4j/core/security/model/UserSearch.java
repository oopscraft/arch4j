package org.oopscraft.arch4j.core.security.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSearch {

    private String userId;

    private String username;

    private String name;

    private boolean admin;

    private User.Status status;

}
