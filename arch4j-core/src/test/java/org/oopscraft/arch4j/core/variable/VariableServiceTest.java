package org.oopscraft.arch4j.core.variable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.variable.repository.VariableEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RequiredArgsConstructor
class VariableServiceTest extends CoreTestSupport {

    final VariableService propertyService;

    Variable testProperty = Variable.builder()
                .id("test")
                .name("test name")
                .build();

    @Test
    @Order(1)
    public void saveProperty() {

        // save
        propertyService.saveVariable(testProperty);

        // check data
        assertNotNull(entityManager.find(VariableEntity.class, testProperty.getId()));
    }

    @Test
    @Order(2)
    public void getProperties() {

        // save test property
        saveProperty();

        // get properties by condition
        VariableSearch propertySearch = VariableSearch.builder()
                .id(testProperty.getId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Variable> page = propertyService.getVariables(propertySearch, pageable);

        // must be existed same id
        assertTrue(page.getContent().stream().anyMatch(e -> e.getId().equals(propertySearch.getId())));
    }

    @Test
    @Order(3)
    public void getProperty() {
        // save
        saveProperty();

        // get property
        Variable property = propertyService.getVariable(testProperty.getId()).orElse(null);

        // test property must be not null
        assertNotNull(property);
    }

    @Test
    @Order(4)
    public void deleteProperty() {
        // save test property
        saveProperty();

        // delete test property
        propertyService.deleteVariable(testProperty.getId());

        // test property must be null
        assertNull(entityManager.find(VariableEntity.class, testProperty.getId()));
    }

}
