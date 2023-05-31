package org.oopscraft.arch4j.web.api.v1.file;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.file.FileInfo;
import org.oopscraft.arch4j.core.file.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@RestController
@RequestMapping("api/v1/file")
@RequiredArgsConstructor
public class FileRestController {

    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<FileInfoResponse> saveFile(@RequestParam("file") MultipartFile multipartFile) {
        String filename = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        Long length = multipartFile.getSize();
        FileInfo fileInfo;
        try (InputStream inputStream = multipartFile.getInputStream()) {
            fileInfo = fileService.saveFile(filename, contentType, length, inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileInfoResponse fileInfoResponse = FileInfoResponse.from(fileInfo);
        return ResponseEntity.ok(fileInfoResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable("id") String id) {
        fileService.deleteFile(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity<Void> getFile(@PathVariable("id") String id, HttpServletResponse response) {
        FileInfo fileInfo = fileService.getFileInfo(id);
        String filename = fileInfo.getFilename();
        try {
            filename = URLEncoder.encode(filename, "UTF-8");
        }catch(UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setHeader("Content-Disposition",String.format("attachment; filename=\"%s\";", filename));
        try (InputStream inputStream = fileService.getFile(fileInfo)) {
            StreamUtils.copy(inputStream, response.getOutputStream());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().build();
    }

}
