package org.oopscraft.arch4j.core.storage;

import java.io.InputStream;

public interface StorageClient {

    void upload(String directory, String filename, InputStream inputStream);

    InputStream download(String directory, String filename);

    void delete(String directory, String filename);

}
