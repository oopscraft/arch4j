package org.oopscraft.arch4j.core.menu;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuSearch {

    private String menuId;

    private String menuName;

}
