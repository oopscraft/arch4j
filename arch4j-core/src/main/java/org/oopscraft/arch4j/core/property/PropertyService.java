package org.oopscraft.arch4j.core.property;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.property.repository.PropertyEntity;
import org.oopscraft.arch4j.core.property.repository.PropertyEntity_;
import org.oopscraft.arch4j.core.property.repository.PropertyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;

    /**
     * saveProperty
     * @param property property
     */
    public void saveProperty(Property property) {
        PropertyEntity propertyEntity = propertyRepository.findById(property.getId()).orElse(null);
        if(propertyEntity == null) {
            propertyEntity = PropertyEntity.builder()
                    .id(property.getId())
                    .build();
        }
        propertyEntity.setName(property.getName());
        propertyEntity.setValue(property.getValue());
        propertyEntity.setNote(property.getNote());
        propertyRepository.saveAndFlush(propertyEntity);
    }

    /**
     * getProperties
     * @param propertySearch property search condition
     * @param pageable pageable
     * @return property page
     */
    public Page<Property> getProperties(PropertySearch propertySearch, Pageable pageable) {

        // specification
        Specification<PropertyEntity> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(propertySearch.getId() != null) {
                predicates.add(criteriaBuilder.like(root.get(PropertyEntity_.ID), '%' + propertySearch.getId() + '%'));
            }
            if(propertySearch.getName() != null) {
                predicates.add(criteriaBuilder.like(root.get(PropertyEntity_.NAME), '%' + propertySearch.getName() + '%'));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // find and return
        Page<PropertyEntity> propertyEntityPage = propertyRepository.findAll(specification, pageable);
        List<Property> properties = propertyEntityPage.getContent().stream()
                .map(Property::from)
                .collect(Collectors.toList());
        long total = propertyEntityPage.getTotalElements();
        return new PageImpl<>(properties, pageable, total);
    }

    /**
     * return property
     * @param id property id
     * @return property
     */
    public Optional<Property> getProperty(String id) {
        return propertyRepository.findById(id).map(Property::from);
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
