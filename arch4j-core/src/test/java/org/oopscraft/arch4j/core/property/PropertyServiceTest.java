package org.oopscraft.arch4j.core.property;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.ServiceTestSupport;
import org.oopscraft.arch4j.core.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RequiredArgsConstructor
class PropertyServiceTest extends ServiceTestSupport {

    final PropertyService propertyService;

    Property createTestProperty() {
        return Property.builder()
                .id("test")
                .name("test name")
                .build();
    }

    @Test
    public void saveProperty() {
        Property property = createTestProperty();
        propertyService.saveProperty(property);
        assertNotNull(entityManager.find(Property.class, property.getId()));
    }

    @Test
    public void getProperties() {

        // save test property
        Property testProperty = createTestProperty();
        entityManager.persist(testProperty);

        // get properties by condition
        PropertySearch propertySearch = PropertySearch.builder()
                .id(testProperty.getId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Property> page = propertyService.getProperties(propertySearch, pageable);

        // must be existed same id
        assertTrue(page.getContent().stream().anyMatch(e -> e.getId().equals(propertySearch.getId())));
    }

    @Test
    public void getProperty() {
        // save
        Property testProperty = createTestProperty();
        entityManager.persist(testProperty);

        // get property
        Property property = propertyService.getProperty(testProperty.getId()).orElse(null);

        // test property must be not null
        assertNotNull(property);
    }

    @Test
    public void deleteProperty() {
        // save test property
        Property testProperty = createTestProperty();
        entityManager.persist(testProperty);

        // delete test property
        propertyService.deleteProperty(testProperty.getId());

        // test property must be null
        assertNull(entityManager.find(Property.class, testProperty.getId()));
    }

}
