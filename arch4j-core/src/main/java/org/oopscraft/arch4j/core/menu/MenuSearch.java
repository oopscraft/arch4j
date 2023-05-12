package org.oopscraft.arch4j.core.menu;

import lombok.*;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuSearch {

    private String id;

    private String name;

}
