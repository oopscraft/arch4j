package org.oopscraft.arch4j.core.file;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.file.repository.FileInfoRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;

@Component("defaultFileService")
@ConditionalOnProperty(prefix = "core.file", name = "bean", havingValue="defaultFileService", matchIfMissing = true)
public class DefaultFileService extends FileService {

    public DefaultFileService(FileInfoRepository fileInfoRepository) {
        super(fileInfoRepository);
    }

    @Override
    protected void uploadFile(FileInfo fileInfo, InputStream inputStream) {

    }

    @Override
    protected void downloadFile(FileInfo fileInfo, OutputStream outputStream) {

    }

}
