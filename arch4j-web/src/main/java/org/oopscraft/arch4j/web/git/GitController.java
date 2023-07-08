package org.oopscraft.arch4j.web.git;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.oopscraft.arch4j.core.git.GitProperties;
import org.oopscraft.arch4j.web.WebProperties;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("git")
@RequiredArgsConstructor
@Slf4j
public class GitController {

    private final GitProperties gitProperties;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @GetMapping("{gitId}/**")
    public Object getMarkdown(@PathVariable("gitId")String gitId, /*@PathVariable("filename")*/String filename, HttpServletRequest request) throws IOException {
        String requestUri = request.getRequestURI();

        String[] requestUriArray = requestUri.split("/");
        String[] filePathArray = new String[requestUriArray.length - 3];
        System.arraycopy(requestUriArray, 3, filePathArray, 0, filePathArray.length);


        Resource resource = getResource(gitId, String.join("/", filePathArray));

        MediaType mediaType = MediaTypeFactory.getMediaType(resource.getFilename())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        if(mediaType.getType().equals("image")){
            byte[] imageBytes = FileUtils.readFileToByteArray(resource.getFile());
            ByteArrayResource byteArrayResource = new ByteArrayResource(imageBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(byteArrayResource);

        }

        ModelAndView modelAndView = new ModelAndView("git/git.html");
        String content = FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8);
        modelAndView.addObject("content", content);
        return modelAndView;
    }

    private Resource getResource(String gitId, String filename) {
        Resource resource = resourceLoader.getResource(String.format("file:%s/%s/%s", gitProperties.getLocation(), gitId, filename));
        if(!resource.exists()){
            log.error("== current pwd: {}", new File(".").getAbsolutePath());
            throw new RuntimeException("resource not found");
        }
        return resource;
    }

}
