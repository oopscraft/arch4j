package org.oopscraft.arch4j.web.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}/article-read")
@RequiredArgsConstructor
public class ArticleReadController {

    private final BoardService boardService;

    @GetMapping
    public ModelAndView index(@PathVariable("boardId")String boardId, @RequestParam("id")String id) {
        Board board = boardService.getBoard(boardId).orElseThrow(() -> new DataNotFoundException(boardId));
        board.setSkin("_default");

        ModelAndView modelAndView = new ModelAndView("board/article-read.html");
        modelAndView.addObject("board", board);
        return modelAndView;
    }

}
