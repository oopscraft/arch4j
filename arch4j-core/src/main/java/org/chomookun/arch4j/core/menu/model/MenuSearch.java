package org.chomookun.arch4j.core.menu.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuSearch {

    private String menuId;

    private String name;

}
