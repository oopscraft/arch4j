package org.oopscraft.arch4j.core.variable.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * VariableRepository
 */
@Repository
public interface VariableRepository extends JpaRepository<VariableEntity, String>, JpaSpecificationExecutor<VariableEntity> {

}
