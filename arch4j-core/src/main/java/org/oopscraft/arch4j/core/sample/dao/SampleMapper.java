package org.oopscraft.arch4j.core.sample.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;
import org.oopscraft.arch4j.core.sample.model.SampleSearch;

import java.util.List;

@Mapper
public interface SampleMapper {

    List<SampleVo> selectSamples(SampleSearch sampleSearch, RowBounds rowBounds);

    Long selectSamplesCount(SampleSearch sampleSearch);

}
