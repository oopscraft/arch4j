package org.chomoo.arch4j.core.git.service;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.chomoo.arch4j.core.common.data.IdGenerator;
import org.chomoo.arch4j.core.git.model.Git;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GitClient {

    private final GitProperties gitProperties;

    public String getDirectoryName(Git git) {
        return IdGenerator.md5(git.getUrl() + '#' + git.getBranch());
    }

    public File getDirectory(Git git) {
        return new File(gitProperties.getLocation() + getDirectoryName(git));
    }

    /**
     * git clone
     * @param git git info
     */
    public void gitClone(Git git) {
        try {
            org.eclipse.jgit.api.Git.cloneRepository()
                    .setURI(git.getUrl())
                    .setBranch(git.getBranch())
                    .setDirectory(getDirectory(git))
                    .call();
        } catch (GitAPIException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * git pull
     * @param git git info
     * @throws IOException io exception
     * @throws GitAPIException git api exception
     */
    public void gitPull(Git git) throws IOException, GitAPIException {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        File gitDir = new File(getDirectory(git).getAbsolutePath() + File.separator + ".git");
        Repository repository = repositoryBuilder
                .setGitDir(gitDir)
                .readEnvironment()
                .findGitDir()
                .build();
        try(org.eclipse.jgit.api.Git jGit = new org.eclipse.jgit.api.Git(repository)) {
            PullCommand pullCommand = jGit.pull();
            PullResult pullResult = pullCommand.call();
        }
    }

}
