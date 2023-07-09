package org.oopscraft.arch4j.core.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearch {

    private String userId;

    private String userName;

    private UserType type;

    private UserStatus status;

    private String email;

    private String mobile;

}
