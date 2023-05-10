package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.property.Property;
import org.oopscraft.arch4j.core.property.PropertySearch;
import org.oopscraft.arch4j.core.property.PropertyService;
import org.oopscraft.arch4j.web.exception.DataNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("admin/property")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('PROPERTY')")
public class PropertyController {

    private final PropertyService propertyService;

    /**
     * index
     * @return model and view
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/property.html");
    }

    /**
     * get properties
     * @return properties
     */
    @GetMapping("get-properties")
    @ResponseBody
    public Page<Property> getProperties(PropertySearch propertySearch, Pageable pageable) {
        return propertyService.getProperties(propertySearch, pageable);
    }

    /**
     * get property
     * @param id property id
     * @return property
     */
    @GetMapping("getProperty")
    @ResponseBody
    public Property getProperty(@RequestParam("id")String id) {
        return propertyService.getProperty(id)
                .orElseThrow(() -> new DataNotFoundException(id));
    }

}
