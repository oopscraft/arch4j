package org.chomookun.arch4j.core.git.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GitSearch {

    private String gitId;

    private String name;

}
