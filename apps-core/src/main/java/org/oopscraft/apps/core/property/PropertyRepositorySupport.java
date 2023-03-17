package org.oopscraft.apps.core.property;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyRepositorySupport {

    Page<Property> findProperties(PropertySearch propertySearch, Pageable pageable);

}