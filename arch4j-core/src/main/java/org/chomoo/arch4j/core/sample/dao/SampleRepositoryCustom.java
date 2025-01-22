package org.chomoo.arch4j.core.sample.dao;

import org.chomoo.arch4j.core.sample.model.SampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleRepositoryCustom {

    Page<SampleEntity> findSamples(SampleSearch sampleSearch, Pageable pageable);

}
