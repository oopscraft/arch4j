package org.oopscraft.arch4j.core.sample.repository;

import org.oopscraft.arch4j.core.sample.Sample;
import org.oopscraft.arch4j.core.sample.SampleSearch;
import org.oopscraft.arch4j.core.sample.SampleType;
import org.oopscraft.arch4j.core.sample.entity.SampleEntity;
import org.oopscraft.arch4j.core.sample.vo.SampleVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleRepositorySupport {

    Page<SampleEntity> findSamples(SampleSearch sampleSearch, Pageable pageable);

}
