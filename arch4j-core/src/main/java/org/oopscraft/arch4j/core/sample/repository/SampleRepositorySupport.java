package org.oopscraft.arch4j.core.sample.repository;

import org.oopscraft.arch4j.core.sample.SampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleRepositorySupport {

    Page<SampleEntity> findSamples(SampleSearch sampleSearch, Pageable pageable);

}
