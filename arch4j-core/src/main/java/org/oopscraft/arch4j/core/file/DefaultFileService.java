package org.oopscraft.arch4j.core.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Component("defaultFileService")
@ConditionalOnProperty(prefix = "core.file", name = "bean", havingValue="defaultFileService", matchIfMissing = true)
public class DefaultFileService implements FileService {

    @Value("${core.file.properties.location}")
    private String location;

    @Override
    public void upload(String directory, String filename, InputStream inputStream) {
        String filepath = location + File.separator + directory + File.separator + filename;
        log.warn("== DefaultFileService.upload:{}", filepath);
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        File file = new FileSystemResource(filepath).getFile();
        try {
            FileUtils.copyInputStreamToFile(inputStream, file);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
        if(!file.setExecutable(false)) {
            throw new RuntimeException("file permission error");
        }
    }

    @Override
    public InputStream download(String directory, String filename) {
        String filepath = location + File.separator + directory + File.separator + filename;
        log.warn("== DefaultFileService.download:{}", filepath);
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        try {
            return new FileSystemResource(filepath).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String directory, String filename) {
        String filepath = location + File.separator + directory + File.separator + filename;
        log.warn("== DefaultFileService.delete:{}", filepath);
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        File file = new File(filepath);
        if(file.exists()) {
            if(!file.delete()) {
                throw new RuntimeException("file delete error");
            };
        }
    }
}
