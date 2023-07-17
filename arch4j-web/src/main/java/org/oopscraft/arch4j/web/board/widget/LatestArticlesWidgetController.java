package org.oopscraft.arch4j.web.board.widget;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.*;
import org.oopscraft.arch4j.core.page.PageWidgetDefinition;
import org.oopscraft.arch4j.core.page.PageWidgetAware;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Properties;

@Controller
@RequestMapping("/board/{boardId}/widget/latest-articles")
@RequiredArgsConstructor
public class LatestArticlesWidgetController extends PageWidgetAware {

    private final BoardService boardService;

    @Override
    public PageWidgetDefinition getDefinition() {
        return PageWidgetDefinition.builder()
                .name("latest-articles")
                .type(this.getClass().getName())
                .propertiesTemplate("boardId=[Board ID]\npageSize=10\n")
                .build();
    }

    @Override
    public String getUrl(Properties properties) {
        String boardId = properties.getProperty("boardId");
        String pageSize = properties.getProperty("pageSize");
        return String.format("/board/%s/widget/latest-articles?_size=%s", boardId, pageSize);
    }

    @GetMapping
    public ModelAndView widget(@PathVariable("boardId") String boardId, @PageableDefault(size = 10) Pageable pageable) {
        Board board = boardService.getBoard(boardId).orElseThrow();
        ModelAndView modelAndView = new ModelAndView("board/widget/latest-articles.html");
        modelAndView.addObject("board", board);
        modelAndView.addObject("pageable", pageable);
        return modelAndView;
    }

}
