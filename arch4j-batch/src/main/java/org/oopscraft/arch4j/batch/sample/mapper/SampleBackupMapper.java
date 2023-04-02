package org.oopscraft.arch4j.batch.sample.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleBackupMapper {

    public Integer insertSampleBackup(SampleBackupVo sampleBackupVo);

}
