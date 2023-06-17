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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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
     * @param articleId article id
     * @return model and view
     */
    @RequestMapping(value = "article-post", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView index(
        @PathVariable("boardId") String boardId,
        @RequestParam(value = "articleId", required = false) String articleId,
        @RequestParam(value = "password", required = false) String password
    ){
        ModelAndView modelAndView = new ModelAndView("board/article-post.html");

        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow(() -> new DataNotFoundException(boardId));
        modelAndView.addObject("board", board);

        // check access permission
        if(!boardService.hasWritePermission(board)){
            throw new AccessDeniedException("No permission");
        }

        // when edit article
        if(articleId != null) {

            // get article
            Article article = articleService.getArticle(articleId).orElseThrow();
            modelAndView.addObject("article", article);

            // when writer is anonymous and password is defined.
            if(article.getUserId() == null) {

                // password not found
                if(password == null) {
                    modelAndView.setViewName("board/article-check-password.html");
                    return modelAndView;
                }

                //check password
                if(!passwordEncoder.matches(password, article.getPassword())) {
                    throw new AuthorizationFailureException("password not matches");
                }

                // append password
                modelAndView.addObject("articlePassword", password);
            }
            // writer is authenticated user, check same with current user
            else{
                // check authenticated
                if(!SecurityUtils.isAuthenticated()) {
                    throw new AuthenticationFailureException("not authenticated");
                }
                // check article writer
                if(!article.getUserId().equals(SecurityUtils.getCurrentUserId())){
                    throw new AuthorizationFailureException("not writer");
                }
            }
        }

        // return
        return modelAndView;
    }

}
