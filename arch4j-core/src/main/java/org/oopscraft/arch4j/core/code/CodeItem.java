package org.oopscraft.arch4j.core.code;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.code.repository.CodeItemEntity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItem {
	
    private String id;

    private int sort;

    private String name;

    private String value;

    static CodeItem from(CodeItemEntity codeItemEntity) {
        return CodeItem.builder()
                .id(codeItemEntity.getId())
                .sort(codeItemEntity.getSort())
                .name(codeItemEntity.getName())
                .value(codeItemEntity.getValue())
                .build();
    }

}
