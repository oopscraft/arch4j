package org.oopscraft.arch4j.batch.sample.dao;

import org.apache.ibatis.annotations.Mapper;
import org.oopscraft.arch4j.batch.sample.model.SampleBackupVo;

@Mapper
public interface SampleBackupMapper {

    public Integer insertSample(SampleBackupVo sampleBackupVo);

}
