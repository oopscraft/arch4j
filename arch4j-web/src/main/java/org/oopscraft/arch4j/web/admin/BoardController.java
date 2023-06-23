package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardSearch;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.web.WebProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin/board")
@PreAuthorize("hasAuthority('ADMIN_BOARD')")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    private final WebProperties webProperties;

    private final ResourceLoader resourceLoader;

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() throws IOException {
        ModelAndView modelAndView = new ModelAndView("admin/board.html");

        // skin names
        Resource skinDir = resourceLoader.getResource(String.format("classpath:templates/theme/%s/board", webProperties.getTheme()));
        List<String> skinNames = Arrays.stream(Objects.requireNonNull(skinDir.getFile().listFiles()))
                .filter(File::isDirectory)
                .map(File::getName)
                .collect(Collectors.toList());
        modelAndView.addObject("skinNames", skinNames);

        // return
        return modelAndView;
    }

    /**
     * get board list
     * @return board
     */
    @GetMapping("get-boards")
    @ResponseBody
    public Page<Board> getBoard(BoardSearch boardSearch, Pageable pageable) {
        return boardService.getBoards(boardSearch, pageable);
    }

    /**
     * get board
     * @param boardId board id
     * @return board
     */
    @GetMapping("get-board")
    @ResponseBody
    public Board getBoard(@RequestParam("boardId")String boardId) {
        return boardService.getBoard(boardId).orElseThrow();
    }

    /**
     * saves board
     * @param board board info
     */
    @PostMapping("save-board")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_BOARD_EDIT')")
    public Board saveVariable(@RequestBody @Valid Board board) {
        return boardService.saveBoard(board);
    }

    /**
     * deletes board
     * @param boardId board id
     */
    @GetMapping("delete-board")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_BOARD_EDIT')")
    public void deleteBoard(@RequestParam("boardId")String boardId) {
        boardService.deleteBoard(boardId);
    }

}
