package org.oopscraft.arch4j.core.sample.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.sample.entity.SampleEntity;
import org.oopscraft.arch4j.core.sample.entity.SampleItemEntity;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RequiredArgsConstructor
public class SampleRepositoryTest extends CoreTestSupport {

    private final SampleRepository sampleRepository;

    SampleEntity testSampleEntity = SampleEntity.builder()
            .id("test_id")
            .name("test_name")
            .item(SampleItemEntity.builder()
                    .sampleId("test_id")
                    .id("item_id")
                    .name("item name")
                    .build())
            .build();

    @Test
    public void save() {
        sampleRepository.saveAndFlush(testSampleEntity);
    }

    @Test
    public void find() {
        // save
        this.save();

        // find
        entityManager.clear();
        SampleEntity sampleEntity = sampleRepository.findById(testSampleEntity.getId())
                .orElseThrow(RuntimeException::new);

        // check
        assertEquals(sampleEntity.toString(), testSampleEntity.toString());
    }

}
