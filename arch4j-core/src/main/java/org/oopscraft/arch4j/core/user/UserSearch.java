package org.oopscraft.arch4j.core.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSearch {

    private String id;

    private String name;

    private String email;

    private String mobile;

    private UserType type;

    private UserStatus status;

}
