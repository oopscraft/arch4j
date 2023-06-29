package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.dao.MenuEntity;
import org.oopscraft.arch4j.core.menu.dao.MenuRepository;
import org.oopscraft.arch4j.core.menu.dao.MenuSpecification;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * save menu
     * @param menu menu
     * @return menu
     */
    public Menu saveMenu(Menu menu) {
        MenuEntity menuEntity = menuRepository.findById(menu.getMenuId()).orElse(null);
        if(menuEntity == null) {
            menuEntity = MenuEntity.builder()
                    .menuId(menu.getMenuId())
                    .build();
        }
        menuEntity = menuEntity.toBuilder()
                .parentMenuId(menu.getParentMenuId())
                .name(menu.getName())
                .link(menu.getLink())
                .target(menu.getTarget())
                .icon(menu.getIcon())
                .sort(menu.getSort())
                .note(menu.getNote())
                .roles(menu.getRoles().stream()
                        .map(role -> RoleEntity.builder()
                                    .roleId(role.getRoleId())
                                    .name(role.getName())
                                    .note(role.getNote())
                                    .build())
                        .collect(Collectors.toList()))
                .build();
        menuEntity = menuRepository.saveAndFlush(menuEntity);
        return Menu.from(menuEntity);
    }

    /**
     * return menu
     * @param menuId menu id
     * @return menu
     */
    public Optional<Menu> getMenu(String menuId) {
        return menuRepository.findById(menuId)
                .map(Menu::from);
    }

    /**
     * delete menu
     * @param menuId menu id
     */
    public void deleteMenu(String menuId) {
        menuRepository.deleteById(menuId);
        menuRepository.flush();
    }

    /**
     * getMenus
     * @param menuSearch menu search condition
     * @param pageable pageable
     * @return menu page
     */
    public Page<Menu> getMenus(MenuSearch menuSearch, Pageable pageable) {
        Specification<MenuEntity> specification = (root, query, criteriaBuilder) -> null;
        if(menuSearch.getMenuId() != null) {
            specification = specification.and(MenuSpecification.likeMenuId(menuSearch.getMenuId()));
        }
        if(menuSearch.getName() != null) {
            specification = specification.and(MenuSpecification.likeName(menuSearch.getName()));
        }
        Page<MenuEntity> menuEntityPage = menuRepository.findAll(specification, pageable);
        List<Menu> menus = menuEntityPage.getContent().stream()
                .map(Menu::from)
                .collect(Collectors.toList());
        long total = menuEntityPage.getTotalElements();
        return new PageImpl<>(menus, pageable, total);
    }

}
