package org.chomoo.arch4j.core.code.model;

import lombok.*;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItem {

    private String codeId;

    private String itemId;

    private String name;

    private int sort;

}
