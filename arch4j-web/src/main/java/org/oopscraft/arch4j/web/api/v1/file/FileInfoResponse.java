package org.oopscraft.arch4j.web.api.v1.file;

import lombok.*;
import org.oopscraft.arch4j.core.file.FileInfo;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileInfoResponse {

    private String id;

    private String name;

    private Long length = 0L;

    public FileInfoResponse from(FileInfo fileInfo) {
        return FileInfoResponse.builder()
                .build();
    }

}
