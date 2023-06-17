package org.oopscraft.arch4j.web.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ModelAndView index(@PathVariable("boardId")String boardId) {

        // get board
        Board board = boardService.getBoard(boardId).orElseThrow();

        // check access permission
        if(!boardService.hasAccessPermission(board)){
            throw new AccessDeniedException("No permission");
        }

        // return model and view
        ModelAndView modelAndView = new ModelAndView("board/board.html");
        modelAndView.addObject("board", board);
        return modelAndView;
    }

}
