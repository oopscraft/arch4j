package org.oopscraft.arch4j.core.property;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    /**
     * saveProperty
     * @param property
     */
    public void saveProperty(Property property) {
        propertyRepository.saveAndFlush(property);
    }

    /**
     * getProperties
     * @param id
     * @param name
     * @param pageable
     * @return
     */
    public Page<Property> getProperties(String id, String name, Pageable pageable) {
        return propertyRepository.findProperties(id, name, pageable);
    }

}
