package org.oopscraft.arch4j.core.sample.repository;

import org.oopscraft.arch4j.core.sample.SampleType;
import org.oopscraft.arch4j.core.sample.repository.SampleEntity_;
import org.springframework.data.jpa.domain.Specification;

public class SampleSpecification {

    public static Specification<SampleEntity> likeId(String id) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(SampleEntity_.ID), '%' + id + '%');
    }

    public static Specification<SampleEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.like(root.get(SampleEntity_.NAME), '%' + name + '%');
    }

    public static Specification<SampleEntity> equalType(SampleType type) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(SampleEntity_.TYPE), type);
    }

}
