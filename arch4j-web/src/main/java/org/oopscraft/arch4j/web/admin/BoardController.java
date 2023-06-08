package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardSearch;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/board")
@PreAuthorize("hasAuthority('ADMIN_BOARD')")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/board.html");
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
     * @param id board id
     * @return board
     */
    @GetMapping("get-board")
    @ResponseBody
    public Board getBoard(@RequestParam("id")String id) {
        return boardService.getBoard(id).orElseThrow(() -> new DataNotFoundException(id));
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
     * @param id board id
     */
    @GetMapping("delete-board")
    @ResponseBody
    @PreAuthorize("hasAuthority('ADMIN_BOARD_EDIT')")
    public void deleteBoard(@RequestParam("id")String id) {
        boardService.deleteBoard(id);
    }

}
