package org.oopscraft.arch4j.core.git;

import lombok.RequiredArgsConstructor;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GitClient {


    private final GitProperties gitProperties;

    /**
     * return git local directory path
     * @param git git info
     * @return git directory path
     */
    public String getGitLocalDirectoryPath(Git git) {
        return gitProperties.getLocation()
                + File.separator
                + git.getGitId();
    }

    /**
     * git clone
     * @param git git info
     */
    public void gitClone(Git git) {
        File directory = new File(gitProperties.getLocation() + File.separator + git.getGitId());
        try {
            org.eclipse.jgit.api.Git.cloneRepository()
                    .setURI(git.getUrl())
                    .setBranch(git.getBranch())
                    .setDirectory(new File(getGitLocalDirectoryPath(git)))
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
        Repository repository = repositoryBuilder.setGitDir(new File(getGitLocalDirectoryPath(git) + "/.git"))
                .readEnvironment()
                .findGitDir()
                .build();
        try(org.eclipse.jgit.api.Git jGit = new org.eclipse.jgit.api.Git(repository)) {
            PullCommand pullCommand = jGit.pull();
            PullResult pullResult = pullCommand.call();
        }
    }

}
