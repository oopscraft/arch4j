package org.oopscraft.arch4j.core.property;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Role repository
 */
@Repository
public interface PropertyRepository extends JpaRepository<Property, String>, JpaSpecificationExecutor<Property> {

}
