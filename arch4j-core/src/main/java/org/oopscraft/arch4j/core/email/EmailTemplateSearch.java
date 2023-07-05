package org.oopscraft.arch4j.core.email;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailTemplateSearch {

    private String templateId;

    private String templateName;

}
