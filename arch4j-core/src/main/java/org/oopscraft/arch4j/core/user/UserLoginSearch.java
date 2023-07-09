package org.oopscraft.arch4j.core.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserLoginSearch {

    private String userId;

    private String ipAddress;

}
