package org.oopscraft.arch4j.core.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Component("defaultFileService")
@ConditionalOnProperty(prefix = "core.file", name = "bean", havingValue="defaultFileService", matchIfMissing = true)
public class DefaultFileService implements FileService {

    @Value("${core.file.properties.location}")
    private String location;

    @Override
    public void upload(String filename, InputStream inputStream) {
        String filepath = location + File.separator + filename;
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
    public InputStream download(String filename) {
        String filepath = location + File.separator + filename;
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        try {
            return new FileSystemResource(filepath).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String filename) {
        String filepath = location + File.separator + filename;
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        File file = new File(filepath);
        if(file.exists()) {
            if(!file.delete()) {
                throw new RuntimeException("file delete error");
            };
        }
    }
}
