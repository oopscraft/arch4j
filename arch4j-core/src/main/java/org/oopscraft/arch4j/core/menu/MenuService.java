package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity_;
import org.oopscraft.arch4j.core.menu.dao.MenuRepository;
import org.oopscraft.arch4j.core.menu.dao.MenuRoleEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final EntityManager entityManager;

    private final MenuRepository menuRepository;

    @Transactional
    public Menu saveMenu(Menu menu) {
        final MenuEntity menuEntity = menuRepository.findById(menu.getMenuId()).orElse(
                MenuEntity.builder()
                        .menuId(menu.getMenuId())
                        .build());

        menuEntity.setParentMenuId(menu.getParentMenuId());
        menuEntity.setMenuName(menu.getMenuName());
        menuEntity.setLink(menu.getLink());
        menuEntity.setTarget(menu.getTarget());
        menuEntity.setIcon(menu.getIcon());
        menuEntity.setSort(menu.getSort());
        menuEntity.setNote(menu.getNote());

        // view role
        menuEntity.setViewPolicy(menu.getViewPolicy());
        menuEntity.getViewRoles().clear();
        menu.getViewRoles().stream()
                .map(role -> MenuRoleEntity.builder()
                        .menuId(menuEntity.getMenuId())
                        .roleId(role.getRoleId())
                        .type("VIEW")
                        .build())
                .forEach(menuEntity.getViewRoles()::add);

        // link role
        menuEntity.setLinkPolicy(menu.getLinkPolicy());
        menuEntity.getLinkRoles().clear();
        menu.getLinkRoles().stream()
                .map(role -> MenuRoleEntity.builder()
                        .menuId(menuEntity.getMenuId())
                        .roleId(role.getRoleId())
                        .type("LINK")
                        .build())
                .forEach(menuEntity.getLinkRoles()::add);

        // save
        MenuEntity savedMenuEntity = menuRepository.saveAndFlush(menuEntity);
        entityManager.clear();

        // return
        return menuRepository.findById(savedMenuEntity.getMenuId())
                .map(Menu::from)
                .orElseThrow();
    }

    public Optional<Menu> getMenu(String menuId) {
        return menuRepository.findById(menuId)
                .map(Menu::from);
    }

    @Transactional
    public void deleteMenu(String menuId) {
        menuRepository.deleteById(menuId);
        menuRepository.flush();
    }

    public List<Menu> getMenus() {
        Sort sort = Sort.by(Sort.Order.asc(MenuEntity_.SORT).nullsLast()); // bug: nullsLast not work
        List<MenuEntity> menuEntities = menuRepository.findAll(sort);
        return menuEntities.stream()
                .map(Menu::from)
                .collect(Collectors.toList());
    }

}
