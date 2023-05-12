package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.menu.Menu;
import org.oopscraft.arch4j.core.menu.MenuSearch;
import org.oopscraft.arch4j.core.menu.MenuService;
import org.oopscraft.arch4j.core.variable.Variable;
import org.oopscraft.arch4j.core.variable.VariableSearch;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("admin/menu")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('PROPERTY')")
public class MenuController {

    private final MenuService menuService;

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
    public Page<Menu> getVariables(MenuSearch menuSearch, Pageable pageable) {
        return menuService.getMenus(menuSearch, pageable);
    }

    /**
     * get variable
     * @param id variable id
     * @return variable
     */
    @GetMapping("get-menu")
    @ResponseBody
    public Menu getVariable(@RequestParam("id")String id) {
        return menuService.getMenu(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

    @PostMapping("save-menu")
    @ResponseBody
    public void saveMenu(@RequestBody @Valid Menu menu) {
        menuService.saveMenu(menu);
    }

    @GetMapping("delete-menu")
    @ResponseBody
    public void deleteMenu(@RequestParam("id")String id) {
        menuService.deleteMenu(id);
    }

}
