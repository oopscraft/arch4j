package org.oopscraft.arch4j.web.api.v1.code.dto;

import lombok.*;
import org.oopscraft.arch4j.core.code.CodeItem;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemResponse {

    private String id;

    private String name;

    private String value;

    static CodeItemResponse from(CodeItem codeItem) {
        return CodeItemResponse.builder()
                .id(codeItem.getItemId())
                .name(codeItem.getItemName())
                .build();
    }

}
