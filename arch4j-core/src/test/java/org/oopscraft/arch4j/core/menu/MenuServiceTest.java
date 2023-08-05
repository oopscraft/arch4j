package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MenuServiceTest extends CoreTestSupport {

    private final MenuService menuService;

    private Menu getTestMenu() {
        Menu testMenu = Menu.builder()
                .menuId("test_menu")
                .parentMenuId(null)
                .menuName("test_name")
                .link("test_link")
                .target(MenuTarget._self)
                .build();
        testMenu.getViewRoles()
                .add(Role.builder()
                        .roleId("view-role")
                        .build());
        testMenu.getLinkRoles()
                .add(Role.builder()
                        .roleId("link-role")
                        .build());
        return testMenu;
    }

    private Menu createTestMenu() {
        Menu testMenu = getTestMenu();
        menuService.saveMenu(testMenu);
        return testMenu;
    }

    @Test
    @Order(1)
    void saveMenu() {
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
    void getMenu() {
        // given
        Menu testMenu = createTestMenu();

        // when
        Menu menu = menuService.getMenu(testMenu.getMenuId()).orElseThrow();

        // then
        assertEquals(testMenu.getMenuId(), menu.getMenuId());
    }

    @Test
    @Order(3)
    void deleteMenu() {
        // given
        Menu testMenu = createTestMenu();

        // when
        menuService.deleteMenu(testMenu.getMenuId());

        // then
        MenuEntity menuEntity = entityManager.find(MenuEntity.class, testMenu.getMenuId());
        assertNull(menuEntity);
    }

}