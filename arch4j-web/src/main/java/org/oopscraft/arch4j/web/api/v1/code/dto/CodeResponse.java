package org.oopscraft.arch4j.web.api.v1.code.dto;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.code.Code;
import org.oopscraft.arch4j.core.code.CodeItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CodeResponse {

    private String codeId;

    private String codeName;

    @Builder.Default
    private List<Item> items = new ArrayList<>();

    @Data
    @Builder
    public static class Item {

        private String id;

        private String name;

        private String value;

        static Item from(CodeItem codeItem) {
            return Item.builder()
                    .id(codeItem.getItemId())
                    .name(codeItem.getItemName())
                    .value(codeItem.getValue())
                    .build();
        }
    }

    public static CodeResponse from(Code code){
         return CodeResponse.builder()
                .codeId(code.getCodeId())
                .codeName(code.getCodeName())
                .items(code.getItems().stream().map(Item::from).collect(Collectors.toList()))
                .build();
    }

}

