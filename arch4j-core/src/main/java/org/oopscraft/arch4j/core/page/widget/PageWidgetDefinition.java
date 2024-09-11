package org.oopscraft.arch4j.core.page.widget;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageWidgetDefinition {

    private String name;

    private String type;

    private String propertiesTemplate;

}
