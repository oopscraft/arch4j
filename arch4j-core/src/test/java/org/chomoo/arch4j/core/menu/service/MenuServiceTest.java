package org.chomoo.arch4j.core.menu.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomoo.arch4j.core.menu.dao.MenuEntity;
import org.chomoo.arch4j.core.menu.model.Menu;
import org.chomoo.arch4j.core.common.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MenuServiceTest extends CoreTestSupport {

    private final MenuService menuService;

    private Menu getTestMenu() {
        Menu testMenu = Menu.builder()
                .menuId("test_menu")
                .parentMenuId(null)
                .name("test_name")
                .link("test_link")
                .target(Menu.Target._self)
                .build();
        return testMenu;
    }

    private Menu saveTestMenu() {
        Menu testMenu = getTestMenu();
        menuService.saveMenu(testMenu);
        return testMenu;
    }

    @Test
    @Order(1)
    void saveMenuToPersist() {
        // given
        Menu testMenu = getTestMenu();

        // when
        menuService.saveMenu(testMenu);

        // then
        MenuEntity menuEntity = entityManager.find(MenuEntity.class, testMenu.getMenuId());
        assertNotNull(menuEntity);
    }

    @Test
    @Order(2)
    void saveMenuToMerge() {
        // given
        Menu testMenu = saveTestMenu();

        // when
        testMenu.setName("changed");
        Menu menu = menuService.saveMenu(testMenu);

        // then
        assertEquals(
                "changed",
                entityManager.find(MenuEntity.class, menu.getMenuId()).getName()
        );
    }

    @Test
    @Order(3)
    void getMenu() {
        // given
        Menu testMenu = saveTestMenu();

        // when
        Menu menu = menuService.getMenu(testMenu.getMenuId()).orElseThrow();

        // then
        assertEquals(testMenu.getMenuId(), menu.getMenuId());
    }

    @Test
    @Order(4)
    void deleteMenu() {
        // given
        Menu testMenu = saveTestMenu();

        // when
        menuService.deleteMenu(testMenu.getMenuId());

        // then
        MenuEntity menuEntity = entityManager.find(MenuEntity.class, testMenu.getMenuId());
        assertNull(menuEntity);
    }

}