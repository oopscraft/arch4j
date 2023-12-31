package org.oopscraft.arch4j.web.admin.view;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.menu.Menu;
import org.oopscraft.arch4j.core.menu.MenuService;
import org.oopscraft.arch4j.core.menu.MenuTarget;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("admin/menu")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN_MENU')")
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ModelAndView menu() {
        ModelAndView modelAndView = new ModelAndView("admin/menu.html");
        modelAndView.addObject("menuTargets", MenuTarget.values());
        return modelAndView;
    }

    @GetMapping("get-menus")
    @ResponseBody
    public List<Menu> getMenus() {
        return menuService.getMenus();
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

}
