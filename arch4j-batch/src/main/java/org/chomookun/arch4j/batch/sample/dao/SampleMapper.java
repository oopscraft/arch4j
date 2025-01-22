package org.chomookun.arch4j.batch.sample.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;

@Mapper
public interface SampleMapper {

    Cursor<SampleVo> selectSamples(@Param("limit") Integer limit);

}
