package org.oopscraft.arch4j.web.api.v1.menu;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.Menu;
import org.oopscraft.arch4j.core.menu.MenuSearch;
import org.oopscraft.arch4j.core.menu.MenuService;
import org.oopscraft.arch4j.web.api.v1.menu.dto.MenuResponse;
import org.oopscraft.arch4j.web.support.PageableUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/menu")
@RequiredArgsConstructor
public class MenuRestController {

    private final MenuService menuService;

    /**
     * return list of menu
     * @param menuSearch menu search condition
     * @param pageable pagination info
     * @return menu list
     */
    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus(MenuSearch menuSearch, Pageable pageable) {
        Page<Menu> menuPage = menuService.getMenus(menuSearch, pageable);
        List<MenuResponse> menuResponses = menuPage.getContent().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_RANGE, PageableUtils.toContentRange("menu",  pageable, menuPage.getTotalElements()))
                .body(menuResponses);
    }

}
