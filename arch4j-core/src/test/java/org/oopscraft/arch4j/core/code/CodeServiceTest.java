package org.oopscraft.arch4j.core.code;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.code.repository.CodeEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleSearch;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

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
        Code savedCode = codeService.saveCode(testCode);
        assertNotNull(savedCode);
        assertNotNull(entityManager.find(CodeEntity.class, savedCode.getCodeId()));
    }

    @Test
    @Order(2)
    void getCode() {
        Code savedCode = codeService.saveCode(testCode);
        Code code = codeService.getCode(savedCode.getCodeId()).orElse(null);
        assertNotNull(code);
    }

    @Test
    @Order(3)
    void deleteCode() {
        Code savedCode = codeService.saveCode(testCode);
        codeService.deleteCode(savedCode.getCodeId());
        assertNull(entityManager.find(CodeEntity.class, testCode.getCodeId()));
    }

    @Test
    @Order(4)
    void getRoles() {
        Code savedCode = codeService.saveCode(testCode);
        CodeSearch codeSearch = CodeSearch.builder()
                .name(savedCode.getName())
                .build();
        Page<Code> codePage = codeService.getCodes(codeSearch, PageRequest.of(0,10));
        assertTrue(codePage.getContent().stream().anyMatch(e -> e.getName().contains(codeSearch.getName())));
    }

}