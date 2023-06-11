package org.oopscraft.arch4j.core.file;

import java.io.InputStream;

public interface FileService {

    void upload(String filename, InputStream inputStream);

    InputStream download(String filename);

    void delete(String filename);

}
