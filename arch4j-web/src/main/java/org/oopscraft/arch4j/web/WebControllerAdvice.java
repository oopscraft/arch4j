package org.oopscraft.arch4j.web;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.LocaleUtils;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class WebControllerAdvice {

    private static final long SCRIPT_VERSION = System.currentTimeMillis();

    private final WebProperties webProperties;

    @ModelAttribute("_scriptVersion")
    public String scriptVersion() {
        return String.valueOf(SCRIPT_VERSION);
    }

    @ModelAttribute("_theme")
    public String theme() {
        return webProperties.getTheme();
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

    /**
     * Returns current login user
     * @return current login user info
     */
    @ModelAttribute("_user")
    @Transactional(readOnly = true)
    public User getUser() {
        return SecurityUtils.getCurrentUser()
                .orElse(new User());
    }

}
