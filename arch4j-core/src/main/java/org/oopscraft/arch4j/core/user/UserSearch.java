package org.oopscraft.arch4j.core.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearch {

    private String username;

    private String nickname;

    private String email;

    private String mobile;

    private UserType type;

    private UserStatus status;

}
