package org.oopscraft.arch4j.core.property;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.ServiceTestSupport;
import org.oopscraft.arch4j.core.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PropertyServiceTest extends ServiceTestSupport {

    private final PropertyService propertyService;

    @Test
    public void saveProperty() {
        Property property = Property.builder()
                .id("test")
                .name("test name")
                .systemUpdateUserId("user")
                .build();
        propertyService.saveProperty(property);
    }

    @Test
    public void getProperties() {

        // save
        this.saveProperty();

        // get list
        PropertySearch propertySearch = PropertySearch.builder()
                .id("test")
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Property> page = propertyService.getProperties(propertySearch, pageable);
        log.info("== content:{}", page.getContent());
        log.info("== totalElements:{}", page.getTotalElements());
    }

}
