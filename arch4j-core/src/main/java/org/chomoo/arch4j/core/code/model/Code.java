package org.chomoo.arch4j.core.code.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.code.dao.CodeEntity;
import org.chomoo.arch4j.core.common.data.BaseModel;

import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code extends BaseModel {
	
	private String codeId;
	
	private String name;
	
	private String note;

    @Builder.Default
	private List<CodeItem> codeItems = new ArrayList<>();

    public static Code from(CodeEntity codeEntity) {
        Code code = Code.builder()
                .systemRequired(codeEntity.isSystemRequired())
                .systemUpdatedAt(codeEntity.getSystemUpdatedAt())
                .systemUpdatedBy(codeEntity.getSystemUpdatedBy())
                .codeId(codeEntity.getCodeId())
                .name(codeEntity.getName())
                .note(codeEntity.getNote())
                .build();

        codeEntity.getCodeItems().forEach(codeItemEntity -> {
            CodeItem codeItem = CodeItem.builder()
                    .codeId(codeItemEntity.getCodeId())
                    .itemId(codeItemEntity.getItemId())
                    .name(codeItemEntity.getName())
                    .sort(codeItemEntity.getSort())
                    .build();
            code.getCodeItems().add(codeItem);
        });
        return code;
    }

}
