package org.chomookun.arch4j.batch.sample.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SampleBackupMapper {

    Integer insertSampleBackup(SampleBackupVo sampleBackupVo);

}
