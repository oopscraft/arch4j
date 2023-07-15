package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity_;
import org.oopscraft.arch4j.core.menu.dao.MenuRepository;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.springframework.data.domain.Sort;
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

        menuEntity.setViewPolicy(menu.getViewPolicy());
        List<RoleEntity> roleEntities = menu.getViewRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .roleName(role.getRoleName())
                        .note(role.getNote())
                        .build())
                .collect(Collectors.toList());
        menuEntity.setViewRoles(roleEntities);

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

    public List<Menu> getMenus() {
        Sort sort = Sort.by(Sort.Order.asc(MenuEntity_.SORT).nullsLast()); // bug: nullsLast not work
        List<MenuEntity> menuEntities = menuRepository.findAll(sort);
        return menuEntities.stream()
                .map(Menu::from)
                .collect(Collectors.toList());
    }

}
