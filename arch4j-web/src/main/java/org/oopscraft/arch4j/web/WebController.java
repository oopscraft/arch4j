package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.security.SecurityPolicy;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class WebController {

    private final WebProperties webProperties;

    /**
     * FIXME index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {

        // check security policy
        if(webProperties.getSecurityPolicy() != null) {
            if(!SecurityUtils.isAuthenticated()) {
                RedirectView redirectView = new RedirectView("/login");
                return new ModelAndView(redirectView);
            }
        }

        // check index page (spring boot bub, yaml ~(null) is converted as empty string)
        if(webProperties.getIndex() != null && webProperties.getIndex().trim().length() > 0) {
            RedirectView redirectView = new RedirectView(webProperties.getIndex());
            return new ModelAndView(redirectView);
        }

        // default page
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("web.html");

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
