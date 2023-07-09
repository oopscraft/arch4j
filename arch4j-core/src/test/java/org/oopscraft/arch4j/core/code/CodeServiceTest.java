package org.oopscraft.arch4j.core.code;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.code.dao.CodeEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class CodeServiceTest extends CoreTestSupport {

    final CodeService codeService;

    Code testCode = Code.builder()
            .codeId("code_id")
            .codeName("role_name")
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
                .codeName(savedCode.getCodeName())
                .build();
        Page<Code> codePage = codeService.getCodes(codeSearch, PageRequest.of(0,10));

        // then
        assertTrue(codePage.getContent().stream()
                .anyMatch(e -> e.getCodeName().contains(codeSearch.getCodeName())));
    }

}