package org.oopscraft.arch4j.core.role;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleSearch {

    private String roleId;

    private String roleName;

}
