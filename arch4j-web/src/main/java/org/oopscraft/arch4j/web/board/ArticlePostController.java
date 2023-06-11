package org.oopscraft.arch4j.web.board;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.oopscraft.arch4j.core.board.Article;
import org.oopscraft.arch4j.core.board.ArticleService;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.oopscraft.arch4j.web.security.exception.AuthenticationFailureException;
import org.oopscraft.arch4j.web.security.exception.AuthorizationFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}")
@RequiredArgsConstructor
public class ArticlePostController {

    private final BoardService boardService;

    private final ArticleService articleService;

    private final PasswordEncoder passwordEncoder;

    /**
     * index
     * @param boardId board id
     * @param id article id
     * @return model and view
     */
    @GetMapping("article-post")
    public ModelAndView index(
        @PathVariable("boardId") String boardId,
        @RequestParam(value = "id", required = false) String id,
        @RequestParam(value = "password", required = false) String password
    ){
        ModelAndView modelAndView = new ModelAndView();

        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow(() -> new DataNotFoundException(boardId));
        modelAndView.addObject("board", board);

        // modify article
        if(id != null) {
            // get article
            Article article = articleService.getArticle(id).orElseThrow(()->new DataNotFoundException(id));
            modelAndView.addObject("article", article);

            // writer is authenticated user
            if(article.getUserId() != null) {
                // check authenticated
                if(!SecurityUtils.isAuthenticated()) {
                    throw new AuthenticationFailureException("not authenticated");
                }
                // check article writer
                if(!article.getUserId().equals(SecurityUtils.getCurrentUserId())){
                    throw new AuthorizationFailureException("not writer");
                }
            }
            // writer is anonymous user
            else {
                // check password
                if(!passwordEncoder.matches(password, article.getPassword())) {
                    throw new AuthorizationFailureException("password not match");
                }
            }
        }

        // return
        modelAndView.setViewName("board/article-post.html");
        return modelAndView;
    }



}
