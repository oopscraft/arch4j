package org.chomoo.arch4j.core.code.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.chomoo.arch4j.core.code.dao.CodeEntity;
import org.chomoo.arch4j.core.code.model.Code;
import org.chomoo.arch4j.core.code.model.CodeSearch;
import org.chomoo.arch4j.core.common.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class CodeServiceTest extends CoreTestSupport {

    final CodeService codeService;

    Code testCode = Code.builder()
            .codeId("code_id")
            .name("role_name")
            .build();

    @Test
    @Order(1)
    void saveCode() {
        // when
        Code savedCode = codeService.saveCode(testCode);

        // then
        assertNotNull(savedCode);
        assertNotNull(entityManager.find(CodeEntity.class, savedCode.getCodeId()));
    }

    @Test
    @Order(2)
    void getCode() {
        // given
        Code savedCode = codeService.saveCode(testCode);

        // when
        Code code = codeService.getCode(savedCode.getCodeId()).orElse(null);

        // then
        assertNotNull(code);
    }

    @Test
    @Order(3)
    void deleteCode() {
        // given
        Code savedCode = codeService.saveCode(testCode);

        // when
        codeService.deleteCode(savedCode.getCodeId());

        // then
        assertNull(entityManager.find(CodeEntity.class, testCode.getCodeId()));
    }

    @Test
    @Order(4)
    void getRoles() {
        // given
        Code savedCode = codeService.saveCode(testCode);

        // when
        CodeSearch codeSearch = CodeSearch.builder()
                .name(savedCode.getName())
                .build();
        Page<Code> codePage = codeService.getCodes(codeSearch, PageRequest.of(0,10));

        // then
        assertTrue(codePage.getContent().stream()
                .anyMatch(e -> e.getName().contains(codeSearch.getName())));
    }

}