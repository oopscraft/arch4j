package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MenuServiceTest extends CoreTestSupport {

    final MenuService menuService;

    Menu testMenu = Menu.builder()
            .menuId("test_menu")
            .parentMenuId(null)
            .menuName("test_name")
            .link("test_link")
            .target(MenuTarget._self)
            .build();

    @Test
    void saveMenu() {
        Menu savedMenu = menuService.saveMenu(testMenu);
        assertNotNull(savedMenu);
        assertNotNull(entityManager.find(MenuEntity.class, testMenu.getMenuId()));
    }

    @Test
    void getMenu() {
        Menu savedMenu = menuService.saveMenu(testMenu);
        Menu menu = menuService.getMenu(savedMenu.getMenuId()).orElse(null);
        assertNotNull(menu);
        assertEquals(savedMenu.getMenuId(), menu.getMenuId());
    }

    @Test
    void deleteMenu() {
        Menu savedMenu = menuService.saveMenu(testMenu);
        menuService.deleteMenu(testMenu.getMenuId());
        assertNull(entityManager.find(MenuEntity.class, testMenu.getMenuId()));
    }

}