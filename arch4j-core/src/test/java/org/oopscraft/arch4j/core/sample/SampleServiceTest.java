package org.oopscraft.arch4j.core.sample;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.sample.dao.SampleEntity;
import org.oopscraft.arch4j.core.support.CoreTestSupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class SampleServiceTest extends CoreTestSupport {

    final SampleService sampleService;

    Sample testSample = Sample.builder()
            .sampleId(UUID.randomUUID().toString())
            .name("name")
            .bigDecimal(BigDecimal.valueOf(1234))
            .build();

    @Test
    @Order(1)
    void saveSample() {
        Sample savedSample = sampleService.saveSample(testSample);
        assertNotNull(savedSample);
        assertNotNull(entityManager.find(SampleEntity.class, savedSample.getSampleId()));
    }

    @Test
    @Order(2)
    void getSample() {
        Sample savedSample = sampleService.saveSample(testSample);
        Sample sample = sampleService.getSample(savedSample.getSampleId()).orElse(null);
        assertNotNull(sample);
        assertEquals(savedSample.getSampleId(), sample.getSampleId());
    }

    @Test
    @Order(3)
    public void deleteSample() {
        Sample savedSample = sampleService.saveSample(testSample);
        sampleService.deleteSample(testSample.getSampleId());
        assertNull(entityManager.find(SampleEntity.class, savedSample.getSampleId()));
    }

    @Test
    @Order(4)
    public void getSamplesByJpa() {
        Sample savedSample = sampleService.saveSample(testSample);
        SampleSearch sampleSearch = SampleSearch.builder()
                .name(savedSample.getName())
                .build();
        Page<Sample> samplePage = sampleService.getSamplesByJpa(sampleSearch, PageRequest.of(0, 10));
        assertTrue(samplePage.getContent().size() > 0);
        assertTrue(samplePage.stream()
                .anyMatch(sample -> sample.getName().contains(sampleSearch.getName()))
        );
    }

    @Test
    @Order(5)
    public void getSamplesByQuerydsl() {
        Sample savedSample = sampleService.saveSample(testSample);
        SampleSearch sampleSearch = SampleSearch.builder()
                .name(savedSample.getName())
                .build();
        Page<Sample> samplePage = sampleService.getSamplesByQuerydsl(sampleSearch, PageRequest.of(0,10));
        assertTrue(samplePage.getContent().size() > 0);
        assertTrue(samplePage.getContent().stream()
                .allMatch(sample -> sample.getName().contains(sampleSearch.getName()))
        );
    }

    @Test
    @Order(6)
    void getSamplesByMybatis() {
        Sample savedSample = sampleService.saveSample(testSample);
        SampleSearch sampleSearch = SampleSearch.builder()
                .name(savedSample.getName())
                .build();
        Page<Sample> samplePage = sampleService.getSamplesByMybatis(sampleSearch, PageRequest.of(0, 10));
        assertTrue(samplePage.getContent().size() > 0);
        assertTrue(samplePage.getContent().stream()
                .allMatch(sample -> sample.getName().contains(sampleSearch.getName()))
        );
    }


}