package org.chomoo.arch4j.core.variable.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomoo.arch4j.core.variable.model.Variable;
import org.chomoo.arch4j.core.variable.model.VariableSearch;
import org.chomoo.arch4j.core.variable.VariableService;
import org.chomoo.arch4j.core.variable.dao.VariableEntity;
import org.chomoo.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@RequiredArgsConstructor
class VariableServiceTest extends CoreTestSupport {

    final VariableService propertyService;

    Variable testProperty = Variable.builder()
                .variableId("test")
                .name("test name")
                .build();

    @Test
    @Order(1)
    public void saveProperty() {

        // when
        propertyService.saveVariable(testProperty);

        // then
        assertNotNull(entityManager.find(VariableEntity.class, testProperty.getVariableId()));
    }

    @Test
    @Order(2)
    public void getProperties() {

        // given
        saveProperty();

        // when
        VariableSearch propertySearch = VariableSearch.builder()
                .variableId(testProperty.getVariableId())
                .build();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Variable> page = propertyService.getVariables(propertySearch, pageable);

        // then
        assertTrue(page.getContent().stream()
                .anyMatch(e -> e.getVariableId().equals(propertySearch.getVariableId())));
    }

    @Test
    @Order(3)
    public void getProperty() {
        // given
        saveProperty();

        // when
        Variable property = propertyService.getVariable(testProperty.getVariableId()).orElse(null);

        // then
        assertNotNull(property);
    }

    @Test
    @Order(4)
    public void deleteProperty() {
        // given
        saveProperty();

        // when
        propertyService.deleteVariable(testProperty.getVariableId());

        // then
        assertNull(entityManager.find(VariableEntity.class, testProperty.getVariableId()));
    }

}
