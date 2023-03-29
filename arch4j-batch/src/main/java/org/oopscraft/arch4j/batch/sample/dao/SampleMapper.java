package org.oopscraft.arch4j.batch.sample.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.oopscraft.arch4j.batch.sample.model.SampleBackupVo;
import org.oopscraft.arch4j.batch.sample.model.SampleVo;

@Mapper
public interface SampleMapper {

    public Cursor<SampleVo> selectSamples(@Param("limit") Integer limit);

    public Integer insertSample(SampleBackupVo sampleBackupVo);

}
