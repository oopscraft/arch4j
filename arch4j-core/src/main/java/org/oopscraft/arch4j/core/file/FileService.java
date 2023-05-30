package org.oopscraft.arch4j.core.file;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.file.repository.FileInfoEntity;
import org.oopscraft.arch4j.core.file.repository.FileInfoRepository;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public abstract class FileService {

    private final FileInfoRepository fileInfoRepository;

    public List<FileInfo> getFileInfos(String targetType, String targetId) {
        return fileInfoRepository.findAllByOwnerTypeAndOwnerIdOrderByCreatedAtAsc(targetType, targetId).stream()
                .map(FileInfo::from)
                .collect(Collectors.toList());
    }

    /**
     * save file
     * @param targetType target type
     * @param targetId target id
     * @param fileInfo file info
     * @param inputStream input stream
     */
    public void saveFile(String targetType, String targetId, FileInfo fileInfo, InputStream inputStream) {
        fileInfo.setOwnerType(targetType);
        fileInfo.setOwnerId(targetId);
        uploadFile(fileInfo, inputStream);
        FileInfoEntity fileInfoEntity = FileInfoEntity.builder()
                .id(fileInfo.getId())
                .filename(fileInfo.getFilename())
                .contentType(fileInfo.getContentType())
                .length(fileInfo.getLength())
                .build();
        fileInfoRepository.saveAndFlush(fileInfoEntity);
    }

    /**
     * get file
     * @param ownerType owner type
     * @param ownerId owner id
     * @param id file id
     * @param outputStream output stream
     */
    public void getFile(String ownerType, String ownerId, String id, OutputStream outputStream) {
        FileInfo fileInfo = fileInfoRepository.findById(id)
                .map(FileInfo::from)
                .orElseThrow(() -> new RuntimeException(id));
        downloadFile(fileInfo, outputStream);
    }

    protected abstract void uploadFile(FileInfo fileInfo, InputStream inputStream);

    protected abstract void downloadFile(FileInfo fileInfo, OutputStream outputStream);

}
