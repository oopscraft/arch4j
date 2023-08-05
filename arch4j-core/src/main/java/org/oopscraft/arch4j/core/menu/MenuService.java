package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity_;
import org.oopscraft.arch4j.core.menu.dao.MenuRepository;
import org.oopscraft.arch4j.core.menu.dao.MenuRoleEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleService;
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

    private final RoleService roleService;

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
        menuEntity.getViewMenuRoleEntities().clear();
        menu.getViewRoles().forEach(viewRole -> {
            MenuRoleEntity menuRoleEntity = MenuRoleEntity.builder()
                    .menuId(menuEntity.getMenuId())
                    .roleId(viewRole.getRoleId())
                    .type("VIEW")
                    .build();
            menuEntity.getViewMenuRoleEntities().add(menuRoleEntity);
        });

        // link role
        menuEntity.setLinkPolicy(menu.getLinkPolicy());
        menuEntity.getLinkMenuRoleEntities().clear();
        menu.getLinkRoles().forEach(linkRole -> {
            MenuRoleEntity menuRoleEntity = MenuRoleEntity.builder()
                    .menuId(menuEntity.getMenuId())
                    .roleId(linkRole.getRoleId())
                    .type("LINK")
                    .build();
            menuEntity.getLinkMenuRoleEntities().add(menuRoleEntity);
        });

        // save
        MenuEntity savedMenu = menuRepository.saveAndFlush(menuEntity);
        return getMenu(savedMenu.getMenuId()).orElseThrow();
    }

    public Optional<Menu> getMenu(String menuId) {
        return menuRepository.findById(menuId)
                .map(this::mapToMenu);
    }

    public Menu mapToMenu(MenuEntity menuEntity) {
        Menu menu = Menu.builder()
                .menuId(menuEntity.getMenuId())
                .menuName(menuEntity.getMenuName())
                .parentMenuId(menuEntity.getParentMenuId())
                .link(menuEntity.getLink())
                .target(menuEntity.getTarget())
                .icon(menuEntity.getIcon())
                .sort(menuEntity.getSort())
                .note(menuEntity.getNote())
                .build();

        MenuEntity parentMenuEntity = menuEntity.getParentMenu();
        if (parentMenuEntity != null) {
            menu.setParentMenuName(parentMenuEntity.getMenuName());
        }

        // view role
        menu.setViewPolicy(menuEntity.getViewPolicy());
        menu.setViewRoles(menuEntity.getViewMenuRoleEntities().stream()
                .map(viewMenuRoleEntity ->
                    roleService.getRole(viewMenuRoleEntity.getRoleId())
                            .orElse(Role.builder()
                                    .roleId(viewMenuRoleEntity.getRoleId())
                                    .build()))
                .collect(Collectors.toList()));

        // link role
        menu.setLinkPolicy(menuEntity.getLinkPolicy());
        menu.setLinkRoles(menuEntity.getLinkMenuRoleEntities().stream()
                .map(linkMenuRoleEntity ->
                        roleService.getRole(linkMenuRoleEntity.getRoleId())
                                .orElse(Role.builder()
                                        .roleId(linkMenuRoleEntity.getRoleId())
                                        .build()))
                .collect(Collectors.toList()));

        // return
        return menu;
    }

    public List<Menu> getMenus() {
        Sort sort = Sort.by(Sort.Order.asc(MenuEntity_.SORT).nullsLast()); // bug: nullsLast not work
        List<MenuEntity> menuEntities = menuRepository.findAll(sort);
        return menuEntities.stream()
                .map(this::mapToMenu)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteMenu(String menuId) {
        menuRepository.deleteById(menuId);
        menuRepository.flush();
    }

}
