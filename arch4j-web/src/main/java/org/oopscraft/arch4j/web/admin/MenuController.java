package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.menu.Menu;
import org.oopscraft.arch4j.core.menu.MenuSearch;
import org.oopscraft.arch4j.core.menu.MenuService;
import org.oopscraft.arch4j.core.menu.MenuTarget;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleSearch;
import org.oopscraft.arch4j.core.role.RoleService;
import org.oopscraft.arch4j.core.user.UserStatus;
import org.oopscraft.arch4j.core.user.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.UUID;

@Controller
@RequestMapping("admin/menu")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN_MENU')")
public class MenuController {

    private final MenuService menuService;

    private final RoleService roleService;

    @GetMapping
    public ModelAndView menu() {
        ModelAndView modelAndView = new ModelAndView("admin/menu.html");
        modelAndView.addObject("menuTargets", MenuTarget.values());
        return modelAndView;
    }

    @GetMapping("get-menus")
    @ResponseBody
    public Page<Menu> getMenus(MenuSearch menuSearch, Pageable pageable) {
        return menuService.getMenus(menuSearch, pageable);
    }

    @GetMapping("get-menu")
    @ResponseBody
    public Menu getMenu(@RequestParam("menuId")String menuId) {
        return menuService.getMenu(menuId)
                .orElseThrow();
    }

    @PostMapping("save-menu")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN_MENU_EDIT')")
    public Menu saveMenu(@RequestBody @Valid Menu menu) {
        if(menu.getMenuId() == null) {
            menu.setMenuId(IdGenerator.uuid());
        }
        return menuService.saveMenu(menu);
    }

    @GetMapping("delete-menu")
    @ResponseBody
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN_MENU_EDIT')")
    public void deleteMenu(@RequestParam("menuId")String menuId) {
        menuService.deleteMenu(menuId);
    }

    @GetMapping("get-roles")
    @ResponseBody
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        return roleService.getRoles(roleSearch, pageable);
    }

}
