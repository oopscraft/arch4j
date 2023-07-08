package org.oopscraft.arch4j.core.git;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.git.dao.GitEntity;
import org.oopscraft.arch4j.core.git.dao.GitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitService {

    private final GitRepository gitRepository;

    public Git saveGit(Git git) {
        GitEntity gitEntity = gitRepository.findById(git.getGitId()).orElse(
                GitEntity.builder()
                    .gitId(git.getGitId())
                    .build());
        gitEntity.setGitName(git.getGitName());
        gitEntity.setUrl(git.getUrl());
        gitEntity.setBranch(git.getBranch());
        gitEntity = gitRepository.saveAndFlush(gitEntity);
        return Git.from(gitEntity);
    }

    public Page<Git> getGits(GitSearch gitSearch, Pageable pageable) {
        Page<GitEntity> gitEntityPage = gitRepository.findAll(gitSearch, pageable);
        List<Git> gits = gitEntityPage.getContent().stream()
                .map(Git::from)
                .collect(Collectors.toList());
        return new PageImpl<>(gits, pageable, gitEntityPage.getTotalElements());
    }

    public List<Git> getGits() {
        return gitRepository.findAll().stream()
                .map(Git::from)
                .collect(Collectors.toList());
    }

    public Optional<Git> getGit(String gitId) {
        return Optional.of(gitRepository.findById(gitId)
                .map(Git::from)
                .orElseThrow());
    }

    public void deleteGit(String gitId) {
        gitRepository.deleteById(gitId);
        gitRepository.flush();
    }


}
