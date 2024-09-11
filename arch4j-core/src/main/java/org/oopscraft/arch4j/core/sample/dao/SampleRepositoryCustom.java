package org.oopscraft.arch4j.core.sample.dao;

import org.oopscraft.arch4j.core.sample.model.SampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleRepositoryCustom {

    Page<SampleEntity> findSamples(SampleSearch sampleSearch, Pageable pageable);

}
