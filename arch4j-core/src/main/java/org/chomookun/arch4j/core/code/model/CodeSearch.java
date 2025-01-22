package org.chomookun.arch4j.core.code.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeSearch {

    private String codeId;

    private String name;

}
