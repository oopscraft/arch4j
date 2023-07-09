package org.oopscraft.arch4j.core.git;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GitSearch {

    private String gitId;

    private String gitName;

}
