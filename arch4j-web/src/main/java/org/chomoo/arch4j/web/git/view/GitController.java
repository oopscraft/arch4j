package org.chomoo.arch4j.web.git.view;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.chomoo.arch4j.core.git.model.Git;
import org.chomoo.arch4j.core.git.service.GitClient;
import org.chomoo.arch4j.core.git.service.GitProperties;
import org.chomoo.arch4j.core.git.service.GitService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Locale;

@Controller
@RequestMapping("git")
@RequiredArgsConstructor
@Slf4j
public class GitController {

    private final GitService gitService;

    private final GitProperties gitProperties;

    private final GitClient gitClient;

    private final LocaleResolver localeResolver;

    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    @GetMapping("{gitId}/**")
    public Object getMarkdown(@PathVariable("gitId")String gitId, HttpServletRequest request) throws IOException {

        // parse request uri
        String requestUri = request.getRequestURI();
        String[] requestUriArray = requestUri.split("/");
        String[] filePathArray = new String[requestUriArray.length - 3];
        System.arraycopy(requestUriArray, 3, filePathArray, 0, filePathArray.length);

        // get git info
        Git git = gitService.getGit(gitId).orElseThrow();

        // get requested git resource
        Resource resource = getResource(git, String.join("/", filePathArray));
        if(!resource.exists()) {
            throw new RuntimeException("resource not found");
        }

        // parse media type
        MediaType mediaType = MediaTypeFactory.getMediaType(resource.getFilename())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        // in case image, send image bytes
        if(mediaType.getType().equals("image")){
            byte[] imageBytes = FileUtils.readFileToByteArray(resource.getFile());
            ByteArrayResource byteArrayResource = new ByteArrayResource(imageBytes);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(byteArrayResource);
        }

        // switch locale markdown
        if(filePathArray[filePathArray.length-1].endsWith(".md")) {
            Locale locale = localeResolver.resolveLocale(request);
            String[] localeFilePathArray = Arrays.copyOf(filePathArray, filePathArray.length);
            String fileName = localeFilePathArray[localeFilePathArray.length-1];
            String localeFileName = FilenameUtils.getBaseName(fileName) +
                    "_" + locale.getLanguage() +
                    "." + FilenameUtils.getExtension(fileName);
            localeFilePathArray[localeFilePathArray.length-1] = localeFileName;
            Resource localeResource = getResource(git, String.join("/", localeFilePathArray));
            if (localeResource.exists()) {
                resource = localeResource;
            }
        }

        // return model and view
        ModelAndView modelAndView = new ModelAndView("git/git.html");
        String content = FileUtils.readFileToString(resource.getFile(), StandardCharsets.UTF_8);
        modelAndView.addObject("content", content);
        return modelAndView;
    }

    private Resource getResource(Git git, String filename) {
        return resourceLoader.getResource(
                "file:" +
                        gitProperties.getLocation()
                        + gitClient.getDirectoryName(git) +
                        File.separator
                        + filename);
    }

}
