package org.oopscraft.arch4j.web.code.api.v1.dto;

import lombok.Builder;
import lombok.Data;
import org.oopscraft.arch4j.core.code.model.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class CodeResponse {

    private String codeId;

    private String codeName;

    @Builder.Default
    private List<CodeItemResponse> codeItems = new ArrayList<>();

    public static CodeResponse from(Code code){
        CodeResponse codeResponse = CodeResponse.builder()
                .codeId(code.getCodeId())
                .codeName(code.getName())
                .build();

        List<CodeItemResponse> codeItems = code.getCodeItems().stream()
                .map(CodeItemResponse::from)
                .collect(Collectors.toList());
        codeResponse.setCodeItems(codeItems);

        return codeResponse;
    }

}

