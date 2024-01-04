package org.oopscraft.arch4j.core.code;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.code.dao.CodeEntity;
import org.oopscraft.arch4j.core.data.BaseModel;

import java.util.ArrayList;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code extends BaseModel {
	
	private String codeId;
	
	private String codeName;
	
	private String note;

    @Builder.Default
	private List<CodeItem> codeItems = new ArrayList<>();

    public static Code from(CodeEntity codeEntity) {
        Code code = Code.builder()
                .systemRequired(codeEntity.isSystemRequired())
                .systemUpdatedAt(codeEntity.getSystemUpdatedAt())
                .systemUpdatedBy(codeEntity.getSystemUpdatedBy())
                .codeId(codeEntity.getCodeId())
                .codeName(codeEntity.getCodeName())
                .note(codeEntity.getNote())
                .build();

        codeEntity.getCodeItems().forEach(codeItemEntity -> {
            CodeItem codeItem = CodeItem.builder()
                    .codeId(codeItemEntity.getCodeId())
                    .itemId(codeItemEntity.getItemId())
                    .itemName(codeItemEntity.getItemName())
                    .sort(codeItemEntity.getSort())
                    .build();
            code.getCodeItems().add(codeItem);
        });
        return code;
    }

}
