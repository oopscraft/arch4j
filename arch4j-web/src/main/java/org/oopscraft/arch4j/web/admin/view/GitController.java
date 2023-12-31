package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.git.Git;
import org.oopscraft.arch4j.core.git.GitSearch;
import org.oopscraft.arch4j.core.git.GitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/git")
@PreAuthorize("hasAuthority('ADMIN_GIT')")
@RequiredArgsConstructor
public class GitController {

    private final GitService gitService;

    @GetMapping
    public ModelAndView git() {
        return new ModelAndView("admin/git.html");
    }

    @GetMapping("get-gits")
    @ResponseBody
    public Page<Git> getGits(GitSearch gitSearch, Pageable pageable) {
        return gitService.getGits(gitSearch, pageable);
    }

    @GetMapping("get-git")
    @ResponseBody
    public Git getGit(@RequestParam("gitId")String gitId) {
        return gitService.getGit(gitId).orElseThrow();
    }

    @PostMapping("save-git")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN_GIT')")
    public Git saveGit(@RequestBody @Valid Git git) {
        return gitService.saveGit(git);
    }

    @GetMapping("delete-git")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN_GIT_EDIT')")
    public void deleteGit(@RequestParam("gitId")String gitId) {
        gitService.deleteGit(gitId);
    }

}
