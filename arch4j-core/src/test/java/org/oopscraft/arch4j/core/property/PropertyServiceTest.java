package org.oopscraft.arch4j.core.property;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.ServiceTestSupport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class PropertyServiceTest extends ServiceTestSupport {

    private final PropertyService propertyService;

    @Test
    public void getProperties() {
        PropertySearch propertySearch = PropertySearch.builder()
                .id("test")
                .build();
        Pageable pageable = PageRequest.of(1, 10);
        List<Property> properties = propertyService.getProperties(propertySearch, pageable);
        log.info("== properties:{}", properties);
    }

}
