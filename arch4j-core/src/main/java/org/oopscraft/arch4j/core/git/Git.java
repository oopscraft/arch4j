package org.oopscraft.arch4j.core.git;

import lombok.*;
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

    private String path;

    public static Git from(GitEntity gitEntity) {
        return Git.builder()
                .gitId(gitEntity.getGitId())
                .gitName(gitEntity.getGitName())
                .note(gitEntity.getNote())
                .url(gitEntity.getUrl())
                .branch(gitEntity.getBranch())
                .path(gitEntity.getPath())
                .build();
    }

}
