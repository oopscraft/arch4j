package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.menu.repository.MenuEntity;
import org.oopscraft.arch4j.core.menu.repository.MenuRepository;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MenuServiceTest extends CoreTestSupport {

    final MenuService menuService;

    Menu testMenu = Menu.builder()
            .id("test_menu")
            .parentId(null)
            .name("test_name")
            .link("test_link")
            .target("blank")
            .build();

    @Test
    void saveMenu() {
        Menu savedMenu = menuService.saveMenu(testMenu);
        assertNotNull(savedMenu);
        assertNotNull(entityManager.find(MenuEntity.class, testMenu.getId()));
    }

    @Test
    void getMenu() {
        Menu savedMenu = menuService.saveMenu(testMenu);
        Menu menu = menuService.getMenu(savedMenu.getId()).orElse(null);
        assertNotNull(menu);
        assertEquals(savedMenu.getId(), menu.getId());
    }

    @Test
    void deleteMenu() {
        Menu savedMenu = menuService.saveMenu(testMenu);
        menuService.deleteMenu(testMenu.getId());
        assertNull(entityManager.find(MenuEntity.class, testMenu.getId()));
    }

}