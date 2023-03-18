package org.oopscraft.arch4j.core.property;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyRepositorySupport {

    Page<Property> findProperties(String id, String name, Pageable pageable);

}
