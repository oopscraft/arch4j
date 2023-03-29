package org.oopscraft.arch4j.core.sample.repository;

import org.oopscraft.arch4j.core.sample.entity.SampleBackupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleBackupRepository extends JpaRepository<SampleBackupEntity, String>, JpaSpecificationExecutor<SampleBackupEntity> {

}
