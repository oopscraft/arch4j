package org.oopscraft.arch4j.core.code;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.code.repository.CodeEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code {
	
	private String id;
	
	private String name;
	
	private String note;

    @Builder.Default
	private List<CodeItem> items = new ArrayList<>();

    static Code from(CodeEntity codeEntity) {
        return Code.builder()
                .id(codeEntity.getId())
                .name(codeEntity.getName())
                .note(codeEntity.getNote())
                .items(codeEntity.getItems().stream()
                        .map(CodeItem::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
