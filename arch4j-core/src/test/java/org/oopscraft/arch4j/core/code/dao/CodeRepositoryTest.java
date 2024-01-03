package org.oopscraft.arch4j.core.code.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.code.CodeSearch;
import org.oopscraft.arch4j.core.support.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class CodeRepositoryTest extends CoreTestSupport {

    private final CodeRepository codeRepository;

    private CodeEntity buildTestCodeEntity() {
        CodeEntity codeEntity = CodeEntity.builder()
                .codeId("test-code")
                .codeName("test code")
                .build();
        Arrays.asList("item-1","item-2").forEach(itemId -> {
            CodeItemEntity codeItemEntity = CodeItemEntity.builder()
                    .codeId(codeEntity.getCodeId())
                    .itemId(itemId)
                    .build();
            codeEntity.getCodeItems().add(codeItemEntity);
        });
        return codeEntity;
    }

    private CodeEntity saveTestCodeEntity() {
        CodeEntity testCodeEntity = buildTestCodeEntity();
        entityManager.persist(testCodeEntity);
        entityManager.flush();
        entityManager.clear();
        return testCodeEntity;
    }

    @Test
    @Order(1)
    void save() {
        // given
        CodeEntity testCodeEntity = buildTestCodeEntity();

        // when
        CodeEntity savedCodeEntity = codeRepository.saveAndFlush(testCodeEntity);
        savedCodeEntity.setCodeName("change code name");
        codeRepository.saveAndFlush(savedCodeEntity);

        // then
        entityManager.clear();
        assertNotNull(entityManager.find(CodeEntity.class, testCodeEntity.getCodeId()));
    }

    @Test
    @Order(2)
    void findById() {
        // given
        CodeEntity testCodeEntity = saveTestCodeEntity();

        // when
        CodeEntity codeEntity = codeRepository.findById(testCodeEntity.getCodeId()).orElseThrow();

        // then
        assertNotNull(codeEntity);
    }

    @Test
    @Order(3)
    void deleteById() {
        // given
        CodeEntity testCodeEntity = saveTestCodeEntity();

        // when
        codeRepository.deleteById(testCodeEntity.getCodeId());
        codeRepository.flush();

        // then
        entityManager.clear();
        assertNull(entityManager.find(CodeEntity.class, testCodeEntity.getCodeId()));
    }

    @Test
    void findAll() {
        // given
        CodeEntity testCodeEntity = saveTestCodeEntity();

        // when
        CodeSearch codeSearch = CodeSearch.builder()
                .codeId(testCodeEntity.getCodeId())
                .build();
        Page<CodeEntity> page = codeRepository.findAll(codeSearch, PageRequest.of(0,10));

        // then
        assertTrue(page.getContent().stream()
                .allMatch(codeEntity ->
                        codeEntity.getCodeId().contains(codeSearch.getCodeId()))
        );
    }

}