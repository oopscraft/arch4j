package org.oopscraft.arch4j.core.property;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    public List<Property> getProperties(PropertySearch codeSearch, Pageable pageable) {
        Page<Property> propertyPage = propertyRepository.findProperties(codeSearch, pageable);
        return propertyPage.getContent();
    }

}
