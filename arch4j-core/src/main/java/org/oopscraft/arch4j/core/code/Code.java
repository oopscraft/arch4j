package org.oopscraft.arch4j.core.code;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.code.entity.CodeEntity;
import org.oopscraft.arch4j.core.code.entity.CodeItemEntity;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@RequiredArgsConstructor
public class Code {
	
	private final String id;
	
	private String name;
	
	private String note;

    @Builder.Default
	List<CodeItem> items = new ArrayList<>();

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
