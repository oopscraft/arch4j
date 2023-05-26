package org.oopscraft.arch4j.web.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ModelAndView listArticle(@PathVariable("boardId")String boardId) {
        Board board = boardService.getBoard(boardId).orElseThrow(() -> new DataNotFoundException(boardId));
        board.setSkin("_default");

        ModelAndView modelAndView = new ModelAndView("board/board.html");
        modelAndView.addObject("board", board);
        return modelAndView;
    }


}
