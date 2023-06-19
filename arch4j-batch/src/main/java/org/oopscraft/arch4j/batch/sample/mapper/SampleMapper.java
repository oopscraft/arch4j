package org.oopscraft.arch4j.batch.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

@Mapper
public interface SampleMapper {

    Cursor<SampleVo> selectSamples(@Param("limit") Integer limit);

}
