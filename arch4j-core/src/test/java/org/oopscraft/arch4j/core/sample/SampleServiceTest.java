package org.oopscraft.arch4j.core.sample;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.sample.entity.SampleEntity;
import org.oopscraft.arch4j.core.test.ServiceTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class SampleServiceTest extends ServiceTestSupport {

    final SampleService sampleService;

    Sample testSample = Sample.builder()
            .id(UUID.randomUUID().toString())
            .name("name")
            .bigDecimal(BigDecimal.valueOf(1234))
            .build();

    @Test
    @Order(1)
    void saveSample() {
        sampleService.saveSample(testSample);
        assertNotNull(entityManager.find(SampleEntity.class, testSample.getId()));
    }

    @Test
    @Order(2)
    void getSample() {

        // save
        saveSample();

        // get sample
        Sample sample = sampleService.getSample(testSample.getId())
                .orElseThrow(RuntimeException::new);

        // check
        assertEquals(testSample.getId(), sample.getId());
    }

    @Test
    @Order(3)
    public void deleteSample() {

        // save
        saveSample();

        // delete
        sampleService.deleteSample(testSample.getId());

        // check
        assertNull(entityManager.find(SampleEntity.class, testSample.getId()));
    }

    @Test
    @Order(4)
    public void getSamples() {

        // save
        saveSample();

        // search
        SampleSearch sampleSearch = SampleSearch.builder()
                .name(testSample.getName())
                .build();
        Page<Sample> samplePage = sampleService.getSamples(sampleSearch, PageRequest.of(0, 10));

        // check
        boolean result = samplePage.stream()
                .anyMatch(sample -> sample.getName().contains(sampleSearch.getName()));
        assertTrue(result);
    }


}