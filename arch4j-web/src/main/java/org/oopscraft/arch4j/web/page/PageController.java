package org.oopscraft.arch4j.web.page;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.page.PageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("page")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping("{pageId}")
    public ModelAndView index() {

        // default page
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("page/page.html");

        List<Map<String,Object>> widgets = new ArrayList<>();
        widgets.add(new HashMap<>(){{
            put("widgetId", "board_anonymous_1");
            put("view", "board/widget.html");
            put("board", new HashMap<String,Object>(){{
                put("boardId", "anonymous");
                put("skin", "_default");
            }});
        }});
        widgets.add(new HashMap<>(){{
            put("widgetId", "board_anonymous_2");
            put("view", "board/widget.html");
            put("board", new HashMap<String,Object>(){{
                put("boardId", "anonymous");
                put("skin", "_default");
            }});
        }});
        widgets.add(new HashMap<>(){{
            put("widgetId", "board_anonymous_3");
            put("view", "board/widget.html");
            put("board", new HashMap<String,Object>(){{
                put("boardId", "anonymous");
                put("skin", "_default");
            }});
        }});
        widgets.add(new HashMap<>(){{
            put("widgetId", "board_anonymous_4");
            put("view", "board/widget.html");
            put("board", new HashMap<String,Object>(){{
                put("boardId", "anonymous");
                put("skin", "_default");
            }});
        }});
        modelAndView.addObject("widgets", widgets);
        return modelAndView;
    }

}
