package org.oopscraft.arch4j.web.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.Article;
import org.oopscraft.arch4j.core.board.ArticleService;
import org.oopscraft.arch4j.core.board.Board;
import org.oopscraft.arch4j.core.board.BoardService;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/board/{boardId}")
@RequiredArgsConstructor
public class ArticlePostController {

    private final BoardService boardService;

    private final ArticleService articleService;

    private final PasswordEncoder passwordEncoder;

    @RequestMapping(value = "article-post", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView articlePost(
        @PathVariable("boardId") String boardId,
        @RequestParam(value = "articleId", required = false) String articleId,
        @RequestParam(value = "password", required = false) String password
    ){
        ModelAndView modelAndView = new ModelAndView("board/article-post.html");

        // get board info
        Board board = boardService.getBoard(boardId).orElseThrow();
        modelAndView.addObject("board", board);

        // check access permission
        boardService.checkWritePermission(board);

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
                    throw new RuntimeException("password not matches");
                }

                // append password
                modelAndView.addObject("articlePassword", password);
            }
            // writer is authenticated user, check same with current user
            else{
                // check authenticated
                if(!SecurityUtils.isAuthenticated()) {
                    throw new RuntimeException("not authenticated");
                }
                // check article writer
                if(!article.getUserId().equals(SecurityUtils.getCurrentUserId())){
                    throw new RuntimeException("not writer");
                }
            }
        }

        // return
        return modelAndView;
    }

}
