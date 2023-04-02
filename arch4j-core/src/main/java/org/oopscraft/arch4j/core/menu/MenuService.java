package org.oopscraft.arch4j.core.menu;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.repository.MenuEntity;
import org.oopscraft.arch4j.core.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    /**
     * save menu
     * @param menu menu
     */
    public void saveMenu(Menu menu) {
        MenuEntity menuEntity = menuRepository.findById(menu.getId()).orElse(null);
        if(menuEntity == null) {
            menuEntity = MenuEntity.builder()
                    .id(menu.getId())
                    .build();
        }
        menuEntity.toBuilder()
                .parentId(menu.getParentId())
                .name(menu.getName())
                .link(menu.getLink())
                .target(menu.getTarget())
                .sort(menu.getSort())
                .build();
        menuRepository.saveAndFlush(menuEntity);
    }

    /**
     * return menu
     * @param id menu id
     * @return menu
     */
    public Optional<Menu> getMenu(String id) {
        return menuRepository.findById(id)
                .map(Menu::from);
    }

    /**
     * delete menu
     * @param id menu id
     */
    public void deleteMenu(String id) {
        menuRepository.deleteById(id);
        menuRepository.flush();
    }

}
