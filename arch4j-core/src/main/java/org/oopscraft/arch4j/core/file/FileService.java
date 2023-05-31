package org.oopscraft.arch4j.core.file;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.file.repository.FileInfoEntity;
import org.oopscraft.arch4j.core.file.repository.FileInfoRepository;
import org.oopscraft.arch4j.core.data.IdGenerator;

import java.io.InputStream;

@RequiredArgsConstructor
public abstract class FileService {

    private final FileInfoRepository fileInfoRepository;

    public FileInfo saveFile(String filename, String contentType, Long length, InputStream inputStream) {
        FileInfo fileInfo = FileInfo.builder()
                .id(IdGenerator.uuid())
                .filename(filename)
                .contentType(contentType)
                .length(length)
                .build();
        internalUpload(fileInfo, inputStream);
        FileInfoEntity fileInfoEntity = FileInfoEntity.builder()
                .id(fileInfo.getId())
                .filename(fileInfo.getFilename())
                .contentType(fileInfo.getContentType())
                .length(fileInfo.getLength())
                .build();
        fileInfoRepository.saveAndFlush(fileInfoEntity);
        return fileInfo;
    }

    public void deleteFile(String id) {
        FileInfo fileInfo = fileInfoRepository.findById(id)
                .map(FileInfo::from)
                .orElseThrow(() -> new RuntimeException(id));
        internalDelete(fileInfo);

    }

    public FileInfo getFileInfo(String id) {
        return fileInfoRepository.findById(id)
                .map(FileInfo::from)
                .orElseThrow(() -> new RuntimeException(id));
    }

    public InputStream getFile(FileInfo fileInfo) {
        return internalDownload(fileInfo);
    }

    protected abstract void internalUpload(FileInfo fileInfo, InputStream inputStream);

    protected abstract InputStream internalDownload(FileInfo fileInfo);

    protected abstract void internalDelete(FileInfo fileInfo);

}
