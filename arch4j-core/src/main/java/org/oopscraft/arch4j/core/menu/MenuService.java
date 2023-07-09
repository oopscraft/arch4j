package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuRepository;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    @Transactional
    public Menu saveMenu(Menu menu) {
        MenuEntity menuEntity = menuRepository.findById(menu.getMenuId()).orElse(
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

        List<RoleEntity> roleEntities = menu.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .roleName(role.getRoleName())
                        .note(role.getNote())
                        .build())
                .collect(Collectors.toList());
        menuEntity.setRoles(roleEntities);

        menuEntity = menuRepository.saveAndFlush(menuEntity);

        return Menu.from(menuEntity);
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

    public Page<Menu> getMenus(MenuSearch menuSearch, Pageable pageable) {
        Page<MenuEntity> menuEntityPage = menuRepository.findAll(menuSearch, pageable);
        List<Menu> menus = menuEntityPage.getContent().stream()
                .map(Menu::from)
                .collect(Collectors.toList());
        return new PageImpl<>(menus, pageable, menuEntityPage.getTotalElements());
    }

}
