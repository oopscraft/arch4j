package org.oopscraft.arch4j.core.git;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.git.dao.GitEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class GitServiceTest extends CoreTestSupport {

    private final GitService gitService;

    private Git testGit = Git.builder()
            .gitId("test_id")
            .gitName("test_name")
            .url("https://github.com/test")
            .build();

    @Test
    public void saveGit() {
        gitService.saveGit(testGit);
        assertNotNull(entityManager.find(GitEntity.class, testGit.getGitId()));
    }

    @Test
    public void getGits() {
        gitService.saveGit(testGit);
        GitSearch gitSearch = GitSearch.builder()
                .gitId(testGit.getGitId())
                .build();
        Page<Git> gitPage = gitService.getGits(gitSearch, Pageable.unpaged());
        assertTrue(gitPage.getContent().stream()
                .allMatch(git -> git.getGitId().contains(testGit.getGitId())));
    }

    @Test
    public void getGit() {
        gitService.saveGit(testGit);
        Git git = gitService.getGit(testGit.getGitId()).orElse(null);
        assertNotNull(git);
    }

}