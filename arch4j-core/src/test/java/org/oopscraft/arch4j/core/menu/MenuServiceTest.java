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

        // save
        menuService.saveMenu(testMenu);

        // check
        assertNotNull(entityManager.find(MenuEntity.class, testMenu.getId()));
    }

    @Test
    void getMenu() {

        // save test menu
        saveMenu();

        // get menu
        Menu menu = menuService.getMenu(testMenu.getId()).orElse(null);

        // check
        assertNotNull(menu);
        assertEquals(testMenu.getId(), menu.getId());
    }

    @Test
    void deleteMenu() {

        // save test
        saveMenu();

        // delete
        menuService.deleteMenu(testMenu.getId());

        // check
        assertNull(entityManager.find(MenuEntity.class, testMenu.getId()));
    }

}