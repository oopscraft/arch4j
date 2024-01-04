package org.oopscraft.arch4j.core.git;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.git.dao.GitEntity;
import org.oopscraft.arch4j.core.git.dao.GitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GitService {

    private final GitRepository gitRepository;

    @Transactional
    public Git saveGit(Git git) {
        GitEntity gitEntity = gitRepository.findById(git.getGitId()).orElse(
                GitEntity.builder()
                    .gitId(git.getGitId())
                    .build());
        gitEntity.setSystemUpdatedAt(LocalDateTime.now());  // disable dirty checking
        gitEntity.setGitName(git.getGitName());
        gitEntity.setUrl(git.getUrl());
        gitEntity.setBranch(git.getBranch());
        GitEntity savedGitEntity = gitRepository.saveAndFlush(gitEntity);
        return Git.from(savedGitEntity);
    }

    public Optional<Git> getGit(String gitId) {
        return Optional.of(gitRepository.findById(gitId)
                .map(Git::from)
                .orElseThrow());
    }

    @Transactional
    public void deleteGit(String gitId) {
        gitRepository.deleteById(gitId);
        gitRepository.flush();
    }

    public List<Git> getGits() {
        return gitRepository.findAll().stream()
                .map(Git::from)
                .collect(Collectors.toList());
    }

    public Page<Git> getGits(GitSearch gitSearch, Pageable pageable) {
        Page<GitEntity> gitEntityPage = gitRepository.findAll(gitSearch, pageable);
        List<Git> gits = gitEntityPage.getContent().stream()
                .map(Git::from)
                .collect(Collectors.toList());
        return new PageImpl<>(gits, pageable, gitEntityPage.getTotalElements());
    }



}
