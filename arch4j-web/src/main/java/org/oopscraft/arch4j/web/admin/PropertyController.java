package org.oopscraft.arch4j.web.admin;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.property.Property;
import org.oopscraft.arch4j.core.property.PropertySearch;
import org.oopscraft.arch4j.core.property.PropertyService;
import org.oopscraft.arch4j.web.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("admin/property")
@RequiredArgsConstructor
public class PropertyController {

    private final PropertyService propertyService;

    /**
     * index
     * @return
     */
    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("admin/property.html");
    }

    /**
     * get properties
     * @return
     */
    @GetMapping("get-properties")
    @ResponseBody
    public Page<Property> getProperties(PropertySearch propertySearch) {
        return propertyService.getProperties(propertySearch, Pageable.unpaged());
    }

    /**
     * get property
     * @param id
     * @return
     */
    @GetMapping("getProperty")
    public Property getProperty(@RequestParam("id")String id) {
        return propertyService.getProperty(id).orElseThrow(()->new ResourceNotFoundException(id));
    }

}
