package org.chomookun.arch4j.core.menu.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomookun.arch4j.core.common.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MenuRepositoryTest extends CoreTestSupport {

    private final MenuRepository menuRepository;

    private MenuEntity getTestMenuEntity() {
        MenuEntity menuEntity = MenuEntity.builder()
                .menuId("test-menu")
                .name("Test Menu")
                .build();
        return menuEntity;
    }

    private MenuEntity saveTestMenuEntity() {
        MenuEntity menuEntity = getTestMenuEntity();
        entityManager.persist(menuEntity);
        entityManager.flush();
        return menuEntity;
    }

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        MenuEntity testMenuEntity = getTestMenuEntity();

        // when
        MenuEntity savedMenuEntity = menuRepository.saveAndFlush(testMenuEntity);

        // then
        entityManager.clear();
        assertNotNull(entityManager.find(MenuEntity.class, savedMenuEntity.getMenuId()));
    }

    @Test
    @Order(2)
    void saveToMerge() {
        // given
        MenuEntity testMenuEntity = saveTestMenuEntity();

        // when
        testMenuEntity.setName("changed");
        menuRepository.saveAndFlush(testMenuEntity);

        // then
        entityManager.clear();
        assertEquals(
                "changed",
                entityManager.find(MenuEntity.class, testMenuEntity.getMenuId()).getName()
        );
    }

    @Test
    @Order(2)
    void findById() {
        // given
        MenuEntity testMenuEntity = saveTestMenuEntity();

        // when
        MenuEntity menuEntity = menuRepository.findById(testMenuEntity.getMenuId())
                .orElseThrow();

        // then
        assertEquals(
                testMenuEntity.getMenuId(),
                menuEntity.getMenuId()
        );
    }

    @Test
    @Order(3)
    void deleteById() {
        // given
        MenuEntity testMenuEntity = saveTestMenuEntity();

        // when
        menuRepository.deleteById(testMenuEntity.getMenuId());

        // then
        entityManager.flush();
        entityManager.clear();
        assertNull(entityManager.find(MenuEntity.class, testMenuEntity.getMenuId()));
    }


}