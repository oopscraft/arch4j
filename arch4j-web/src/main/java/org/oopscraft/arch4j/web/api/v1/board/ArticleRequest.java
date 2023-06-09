package org.oopscraft.arch4j.web.api.v1.board;

import lombok.*;
import org.oopscraft.arch4j.web.api.v1.file.FileInfoRequest;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ArticleRequest {

    private String id;

    private String title;

    private String content;

    private String userName;

    private String password;

    @Builder.Default
    private List<FileInfoRequest> files = new ArrayList<>();

}
