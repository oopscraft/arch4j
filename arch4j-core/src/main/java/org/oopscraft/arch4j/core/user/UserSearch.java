package org.oopscraft.arch4j.core.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserSearch {

    private String userId;

    private String userName;

    private UserType userType;

    private boolean admin;

    private String email;

    private String mobile;

}
