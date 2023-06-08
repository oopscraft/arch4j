package org.oopscraft.arch4j.web.api.v1.file;

import lombok.*;
import org.oopscraft.arch4j.core.file.FileInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileInfoRequest {

    private String id;

    private String filename;

    private String contentType;

    @Builder.Default
    private Long length = 0L;

}
