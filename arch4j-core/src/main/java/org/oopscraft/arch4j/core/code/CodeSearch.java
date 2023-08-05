package org.oopscraft.arch4j.core.code;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeSearch {

    private String codeId;

    private String codeName;

}
