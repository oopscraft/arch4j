package org.oopscraft.arch4j.core.sample.repository;

import org.oopscraft.arch4j.core.sample.Sample;
import org.oopscraft.arch4j.core.sample.SampleType;
import org.oopscraft.arch4j.core.sample.vo.SampleVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SampleRepositorySupport {

    Page<SampleVo> findSamples(String id, String name, SampleType type, Pageable pageable);

}
