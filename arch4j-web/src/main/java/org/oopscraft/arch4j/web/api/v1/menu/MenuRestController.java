package org.oopscraft.arch4j.web.api.v1.menu;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.MenuService;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.oopscraft.arch4j.web.api.v1.menu.dto.MenuResponse;
import org.oopscraft.arch4j.web.support.PageableUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/menus")
@RequiredArgsConstructor
@Tag(name = "menu", description = "Menu")
public class MenuRestController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus() {
        List<MenuResponse> menuResponses = menuService.getMenus().stream()
                .filter(menu -> SecurityUtils.hasPermission(menu.getViewPolicy(), menu.getViewRoles()))
                .peek(menu -> {
                    if (!SecurityUtils.hasPermission(menu.getLinkPolicy(), menu.getLinkRoles())) {
                        menu.setLink(null);
                        menu.setTarget(null);
                    }
                })
                .map(MenuResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("menu",  null, menuResponses.size()))
                .body(menuResponses);
    }

}
