package org.oopscraft.arch4j.core.code;

import lombok.*;
import org.oopscraft.arch4j.core.code.dao.CodeItemEntity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItem {

    private String codeId;

    private String itemId;

    private String itemName;

    private int sort;

    private String value;

    static CodeItem from(CodeItemEntity codeItemEntity) {
        return CodeItem.builder()
                .codeId(codeItemEntity.getCodeId())
                .itemId(codeItemEntity.getItemId())
                .itemName(codeItemEntity.getItemName())
                .sort(codeItemEntity.getSort())
                .value(codeItemEntity.getValue())
                .build();
    }

}
