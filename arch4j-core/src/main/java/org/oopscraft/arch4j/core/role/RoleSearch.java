package org.oopscraft.arch4j.core.role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleSearch {

    private String roleId;

    private String name;

}
