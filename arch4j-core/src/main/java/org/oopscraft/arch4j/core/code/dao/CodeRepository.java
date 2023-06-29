package org.oopscraft.arch4j.core.code.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository 
public interface CodeRepository extends JpaRepository<CodeEntity,String>, JpaSpecificationExecutor<CodeEntity> {

}
