package org.oopscraft.arch4j.core.user;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class UserLoginSearch {

    private String userId;

    private String ipAddress;

}
