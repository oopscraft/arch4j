package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.LocaleUtils;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.core.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@ControllerAdvice
@RequiredArgsConstructor
public class WebController {

    private static final long SCRIPT_VERSION = System.currentTimeMillis();

    private final WebProperties webProperties;

    @GetMapping
    public ModelAndView index() {

        // check security policy
        if(webProperties.getSecurityPolicy() != null) {
            if(!SecurityUtils.isAuthenticated()) {
                RedirectView redirectView = new RedirectView("/login");
                return new ModelAndView(redirectView);
            }
        }

        // redirect index
        RedirectView redirectView = new RedirectView(webProperties.getIndex());
        return new ModelAndView(redirectView);
    }

    @ModelAttribute("_scriptVersion")
    public String scriptVersion() {
        return String.valueOf(SCRIPT_VERSION);
    }

    @ModelAttribute("_theme")
    public String theme() {
        return webProperties.getTheme();
    }

    @ModelAttribute("_brand")
    public String brand() {
        return webProperties.getBrand();
    }

    @ModelAttribute("_copyright")
    public String copyright() {
        return "Copyright (C) 2016 oopscraft.org.\nunder Apache License, Version 2.0";
    }

    @ModelAttribute("_locales")
    public List<Map<String,String>> locales() {
        return webProperties.getLocales().stream()
                .map(value -> {
                    Locale locale = LocaleUtils.toLocale(value);
                    return new LinkedHashMap<String,String>(){{
                        put("language", locale.getLanguage());
                        put("displayLanguage", locale.getDisplayLanguage(locale));
                        put("country", locale.getCountry());
                        put("displayCountry", locale.getDisplayCountry(locale));
                    }};
                })
                .collect(Collectors.toList());
    }

    @ModelAttribute("_locale")
    public Locale getLocale(HttpServletRequest request) {
        return Objects.requireNonNull(RequestContextUtils.getLocaleResolver(request)).resolveLocale(request);
    }

    @ModelAttribute("_user")
    @Transactional(readOnly = true)
    public User getUser() {
        return SecurityUtils.getCurrentUser()
                .orElse(new User());
    }

}
