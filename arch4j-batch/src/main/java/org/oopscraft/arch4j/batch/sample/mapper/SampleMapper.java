package org.oopscraft.arch4j.batch.sample.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.oopscraft.arch4j.batch.sample.vo.SampleBackupVo;
import org.oopscraft.arch4j.batch.sample.vo.SampleVo;

@Mapper
public interface SampleMapper {

    public Cursor<SampleVo> selectSamples(@Param("limit") Integer limit);

}
