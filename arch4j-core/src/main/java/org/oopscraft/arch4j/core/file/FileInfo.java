package org.oopscraft.arch4j.core.file;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.file.repository.FileInfoEntity;

import java.io.OutputStream;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileInfo extends SystemFieldEntity {

    private String id;

    private String filename;

    private String contentType;

    @Builder.Default
    private Long length = 0L;

    /**
     * factory method
     * @param fileInfoEntity file info entity
     * @return file info
     */
    public static FileInfo from(FileInfoEntity fileInfoEntity) {
        return FileInfo.builder()
                .id(fileInfoEntity.getId())
                .filename(fileInfoEntity.getFilename())
                .contentType(fileInfoEntity.getContentType())
                .length(fileInfoEntity.getLength())
                .build();
   }

}
