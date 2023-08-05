package org.oopscraft.arch4j.core.git;

import lombok.*;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.git.dao.GitEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Git {

    private String gitId;

    private String gitName;

    private String note;

    private String url;

    private String branch;

}
