package org.chomookun.arch4j.web.code.api.v1.dto;

import lombok.*;
import org.chomookun.arch4j.core.code.model.CodeItem;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemResponse {

    private String itemId;

    private String itemName;

    private String value;

    static CodeItemResponse from(CodeItem codeItem) {
        return CodeItemResponse.builder()
                .itemId(codeItem.getItemId())
                .itemName(codeItem.getName())
                .build();
    }

}
