package org.oopscraft.arch4j.core.property;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    /**
     * saveProperty
     * @param property property
     */
    public void saveProperty(Property property) {
        Property one = propertyRepository.findById(property.getId()).orElse(null);
        if(one == null) {
            one = Property.builder()
                    .id(property.getId())
                    .build();
        }
        one.setName(property.getName());
        one.setValue(property.getValue());
        one.setNote(property.getNote());
        propertyRepository.saveAndFlush(one);
    }

    /**
     * getProperties
     * @param propertySearch property search condition
     * @param pageable pageable
     * @return property page
     */
    public Page<Property> getProperties(PropertySearch propertySearch, Pageable pageable) {

        // specification
        Specification<Property> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(propertySearch.getId() != null) {
                predicates.add(criteriaBuilder.like(root.get(Property_.ID), '%' + propertySearch.getId() + '%'));
            }
            if(propertySearch.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get(Property_.NAME), '%' + propertySearch.getName() + '%'));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // find and return
        return propertyRepository.findAll(specification, pageable);
    }

    /**
     * return property
     * @param id property id
     * @return property
     */
    public Optional<Property> getProperty(String id) {
        return propertyRepository.findById(id);
    }

    /**
     * delete property
     * @param id property id
     */
    public void deleteProperty(String id) {
        propertyRepository.deleteById(id);
        propertyRepository.flush();
    }

}
