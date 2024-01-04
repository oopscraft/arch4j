package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity_;
import org.oopscraft.arch4j.core.menu.dao.MenuRepository;
import org.oopscraft.arch4j.core.menu.dao.MenuRoleEntity;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public Menu saveMenu(Menu menu) {
        final MenuEntity menuEntity = menuRepository.findById(menu.getMenuId()).orElse(
                MenuEntity.builder()
                        .menuId(menu.getMenuId())
                        .build());
        menuEntity.setSystemUpdatedAt(LocalDateTime.now()); // disable dirty checking
        menuEntity.setParentMenuId(menu.getParentMenuId());
        menuEntity.setMenuName(menu.getMenuName());
        menuEntity.setLink(menu.getLink());
        menuEntity.setTarget(menu.getTarget());
        menuEntity.setIcon(menu.getIcon());
        menuEntity.setSort(menu.getSort());
        menuEntity.setNote(menu.getNote());

        // view role
        menuEntity.getViewMenuRoles().clear();
        menu.getViewRoles().forEach(viewRole -> {
            MenuRoleEntity menuRoleEntity = MenuRoleEntity.builder()
                    .menuId(menuEntity.getMenuId())
                    .roleId(viewRole.getRoleId())
                    .type("VIEW")
                    .build();
            menuEntity.getViewMenuRoles().add(menuRoleEntity);
        });

        // link role
        menuEntity.getLinkMenuRoles().clear();
        menu.getLinkRoles().forEach(linkRole -> {
            MenuRoleEntity menuRoleEntity = MenuRoleEntity.builder()
                    .menuId(menuEntity.getMenuId())
                    .roleId(linkRole.getRoleId())
                    .type("LINK")
                    .build();
            menuEntity.getLinkMenuRoles().add(menuRoleEntity);
        });

        MenuEntity savedMenu = menuRepository.saveAndFlush(menuEntity);
        return Menu.from(savedMenu);
    }

    public Optional<Menu> getMenu(String menuId) {
        return menuRepository.findById(menuId)
                .map(Menu::from);
    }

    public List<Menu> getMenus() {
        Sort sort = Sort.by(Sort.Order.asc(MenuEntity_.SORT).nullsLast()); // bug: nullsLast not work
        List<MenuEntity> menuEntities = menuRepository.findAll(sort);
        return menuEntities.stream()
                .map(Menu::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMenu(String menuId) {
        menuRepository.deleteById(menuId);
        menuRepository.flush();
    }

}
