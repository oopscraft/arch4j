package org.oopscraft.arch4j.core.storage;

import lombok.RequiredArgsConstructor;
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
@Component("defaultStorageClient")
@ConditionalOnProperty(prefix = "core.storage.storage-client", name = "bean", havingValue="defaultStorageClient", matchIfMissing = true)
@RequiredArgsConstructor
public class DefaultStorageClient implements StorageClient {

    private final StorageClientProperties storageClientProperties;

    /**
     * get location
     * @return location
     */
    private String getLocation() {
        return storageClientProperties.getProperties().getProperty("location");
    }

    @Override
    public void upload(String directory, String filename, InputStream inputStream) {
        String filepath = getLocation() + File.separator + directory + File.separator + filename;
        log.debug("== DefaultStorageClient.upload:{}", filepath);
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
        String filepath = getLocation() + File.separator + directory + File.separator + filename;
        log.debug("== DefaultStorageClient.download:{}", filepath);
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        try {
            return new FileSystemResource(filepath).getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(String directory, String filename) {
        String filepath = getLocation() + File.separator + directory + File.separator + filename;
        log.debug("== DefaultStorageClient.delete:{}", filepath);
        filepath = FilenameUtils.normalizeNoEndSeparator(filepath);
        File file = new File(filepath);
        if(file.exists()) {
            if(!file.delete()) {
                throw new RuntimeException("file delete error");
            };
        }
    }
}
