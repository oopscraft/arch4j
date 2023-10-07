package org.oopscraft.arch4j.web.git;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.oopscraft.arch4j.core.git.Git;
import org.oopscraft.arch4j.core.git.GitClient;
import org.oopscraft.arch4j.core.git.GitProperties;
import org.oopscraft.arch4j.core.git.GitService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GitScheduler {

    private final GitService gitService;

    private final GitClient gitClient;

    private final GitProperties gitProperties;

    @Scheduled(initialDelay = 1_000, fixedDelay = 60_000)
    public void synchronizeGits() {
        List<Git> gits = gitService.getGits();
        for(Git git : gits) {
            try {
                synchronizeGit(git);
            } catch(Throwable t) {
                log.warn(t.getMessage(), t);
            }
        }

        // delete data not existing
        getExistingGitLocalDirectories().forEach(gitLocalDirectory -> {
            if(gits.stream().noneMatch(git -> gitClient.getDirectoryName(git).equals(gitLocalDirectory.getName()))) {
                try {
                    FileUtils.deleteDirectory(gitLocalDirectory);
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        });
    }

    private void synchronizeGit(Git git) throws Exception {
        String gitDirectory = gitProperties.getLocation() + gitClient.getDirectoryName(git);
        if(!new File(gitDirectory).exists()) {
            gitClient.gitClone(git);
        }else{
            gitClient.gitPull(git);
        }
    }

    private List<File> getExistingGitLocalDirectories() {
        File locationDirectory = new File(gitProperties.getLocation());
        return Arrays.stream(Optional.ofNullable(locationDirectory.listFiles(File::isDirectory)).orElse(new File[]{}))
                .collect(Collectors.toList());
    }

}
