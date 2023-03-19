package org.oopscraft.arch4j.core.property;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
     * @param propertySearch
     * @param pageable
     * @return
     */
    public Page<Property> getProperties(PropertySearch propertySearch, Pageable pageable) {

        // specification
        Specification specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(propertySearch.getId() != null) {
                predicates.add(criteriaBuilder.like(root.get(Property_.ID), '%' + propertySearch.getId() + '%'));
            }
            if(propertySearch.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get(Property_.NAME), '%' + propertySearch.getName() + '%'));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };

        // find and return
        return propertyRepository.findAll(specification, pageable);
    }

    /**
     * getProperty
     * @param id
     * @return
     */
    public Optional<Property> getProperty(String id) {
        return propertyRepository.findById(id);
    }

}
