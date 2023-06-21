package org.oopscraft.arch4j.core.code.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
public class CodeEntityTest extends CoreTestSupport {

    @Test
    public void testSave() {

        // persist
        CodeEntity codeEntity = CodeEntity.builder()
                .codeId("test id")
                .name("test name")
                .build();

        codeEntity.setItems(IntStream.range(0,10)
                .mapToObj(i -> CodeItemEntity.builder()
                        .codeId(codeEntity.getCodeId())
                        .itemId(String.format("item %s",i))
                        .name(String.format("name %d", i))
                        .build())
                .collect(Collectors.toList()));

        entityManager.persist(codeEntity);
        entityManager.flush();

        // find
        CodeEntity savedCodeEntity = entityManager.find(CodeEntity.class, codeEntity.getCodeId());
        log.debug("{}", savedCodeEntity);
        assertNotNull(savedCodeEntity);
    }

}
