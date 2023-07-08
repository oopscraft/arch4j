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

    /**
     * synchronize git repositories
     */
    @Scheduled(fixedDelay = 1000*10, initialDelay = 1000*10)
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
            if(gits.stream().noneMatch(git->git.getGitId().equals(gitLocalDirectory.getName()))) {
                try {
                    FileUtils.deleteDirectory(gitLocalDirectory);
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        });
    }

    /**
     * synchronize single git repository
     * @param git git info
     * @throws Exception exception
     */
    private void synchronizeGit(Git git) throws Exception {
        String gitDirectory = gitClient.getGitLocalDirectoryPath(git);
        if(!new File(gitDirectory).exists()) {
            gitClient.gitClone(git);
        }else{
            gitClient.gitPull(git);
        }
    }

    /**
     * get git local directory names
     * @return local git directory names
     */
    private List<File> getExistingGitLocalDirectories() {
        File locationDirectory = new File(gitProperties.getLocation());
        return Arrays.stream(Optional.ofNullable(locationDirectory.listFiles(File::isDirectory)).orElse(new File[]{}))
                .collect(Collectors.toList());
    }

}
