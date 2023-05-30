package org.oopscraft.arch4j.core.file;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.file.repository.FileInfoEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FileInfo extends SystemFieldEntity {

    private String id;

    private String ownerType;

    private String ownerId;

    private String filename;

    private String contentType;

    private Long length = 0L;

    /**
     * factory method
     * @param fileInfoEntity file info entity
     * @return file info
     */
    public static FileInfo from(FileInfoEntity fileInfoEntity) {
        return FileInfo.builder()
                .id(fileInfoEntity.getId())
                .ownerType(fileInfoEntity.getOwnerType())
                .ownerId(fileInfoEntity.getOwnerId())
                .filename(fileInfoEntity.getFilename())
                .contentType(fileInfoEntity.getContentType())
                .length(fileInfoEntity.getLength())
                .build();
    }

}
