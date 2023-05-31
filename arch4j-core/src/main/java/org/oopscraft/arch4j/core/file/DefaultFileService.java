package org.oopscraft.arch4j.core.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.oopscraft.arch4j.core.file.repository.FileInfoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Component("defaultFileService")
@ConditionalOnProperty(prefix = "core.file", name = "bean", havingValue="defaultFileService", matchIfMissing = true)
public class DefaultFileService extends FileService {

    @Value("${core.file.properties.location}")
    private String location;

    public DefaultFileService(FileInfoRepository fileInfoRepository) {
        super(fileInfoRepository);
    }

    @Override
    protected void internalUpload(FileInfo fileInfo, InputStream inputStream) {
        String filepath = location + File.separator + fileInfo.getId();
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        File file = new FileSystemResource(filepath).getFile();
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(!file.setExecutable(false)) {
            throw new RuntimeException("file permission error");
        }
    }

    @Override
    protected InputStream internalDownload(FileInfo fileInfo) {
        String filepath = location + File.separator + fileInfo.getId();
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        try {
            return new FileSystemResource(filepath).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void internalDelete(FileInfo fileInfo) {
        String filepath = location + File.separator + fileInfo.getId();
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        File file = new File(filepath);
        if(file.exists()) {
            if(!file.delete()) {
                throw new RuntimeException("file delete error");
            };
        }
    }

}
