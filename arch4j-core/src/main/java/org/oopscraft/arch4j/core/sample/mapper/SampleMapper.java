package org.oopscraft.arch4j.core.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.oopscraft.arch4j.core.sample.vo.SampleVo;

@Mapper
public interface SampleMapper {

    public Cursor<SampleVo> selectSamples(@Param("limit") Integer limit);

}
