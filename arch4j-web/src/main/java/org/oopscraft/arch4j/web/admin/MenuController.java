package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.Menu;
import org.oopscraft.arch4j.core.menu.MenuSearch;
import org.oopscraft.arch4j.core.menu.MenuService;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleSearch;
import org.oopscraft.arch4j.core.role.RoleService;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("admin/menu")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ADMIN_MENU')")
public class MenuController {

    private final MenuService menuService;

    private final RoleService roleService;

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/menu.html");
    }

    /**
     * get menus
     * @return menus
     */
    @GetMapping("get-menus")
    @ResponseBody
    public Page<Menu> getMenus(MenuSearch menuSearch, Pageable pageable) {
        return menuService.getMenus(menuSearch, pageable);
    }

    /**
     * get menu
     * @param id menu id
     * @return menu
     */
    @GetMapping("get-menu")
    @ResponseBody
    public Menu getMenu(@RequestParam("id")String id) {
        return menuService.getMenu(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    /**
     * saves menu
     * @param menu menu info
     */
    @PostMapping("save-menu")
    @ResponseBody
    public void saveMenu(@RequestBody @Valid Menu menu) {
        if(menu.getId() == null) {
            menu.setId(UUID.randomUUID().toString());
        }
        menuService.saveMenu(menu);
    }

    /**
     * deletes menu
     * @param id menu id
     */
    @GetMapping("delete-menu")
    @ResponseBody
    public void deleteMenu(@RequestParam("id")String id) {
        menuService.deleteMenu(id);
    }

    /**
     * get roles
     * @param roleSearch role search condition
     * @param pageable page info
     */
    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

}
