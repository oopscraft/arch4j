package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardSearch;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.web.WebProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("admin/board")
@PreAuthorize("hasAuthority('ADMIN_BOARD')")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final WebProperties webProperties;

    @GetMapping
    public ModelAndView board() throws IOException {
        ModelAndView modelAndView = new ModelAndView("admin/board.html");
        modelAndView.addObject("skinNames", getSkinNames());
        return modelAndView;
    }

    protected Set<String> getSkinNames() throws IOException {
        Set<String> skinNames = new HashSet<>();
        String resourcePattern = String.format("classpath*:templates/_theme/%s/board/**/board.html", webProperties.getTheme());
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources(resourcePattern);
        for (Resource resource : resources) {
            String resourcePath = resource.getURL().getPath();
            String[] pathParts = resourcePath.split("/");
            String lastDirectoryName = pathParts[pathParts.length - 2];
            skinNames.add(lastDirectoryName);
        }
        return skinNames;
    }

    @GetMapping("get-boards")
    @ResponseBody
    public Page<Board> getBoard(BoardSearch boardSearch, Pageable pageable) {
        return boardService.getBoards(boardSearch, pageable);
    }

    @GetMapping("get-board")
    @ResponseBody
    public Board getBoard(@RequestParam("boardId")String boardId) {
        return boardService.getBoard(boardId).orElseThrow();
    }

    @PostMapping("save-board")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_BOARD_EDIT')")
    @Transactional
    public Board saveVariable(@RequestBody @Valid Board board) {
        return boardService.saveBoard(board);
    }

    @GetMapping("delete-board")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_BOARD_EDIT')")
    @Transactional
    public void deleteBoard(@RequestParam("boardId")String boardId) {
        boardService.deleteBoard(boardId);
    }

}
