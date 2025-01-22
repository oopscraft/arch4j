package org.chomoo.arch4j.core.sample.dao;

import org.chomoo.arch4j.core.sample.model.SampleType;
import org.springframework.data.jpa.domain.Specification;

public class SampleSpecification {

    public static Specification<SampleEntity> likeSampleId(String sampleId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(SampleEntity_.SAMPLE_ID), '%' + sampleId + '%');
    }

    public static Specification<SampleEntity> likeName(String name) {
        return (root, query, criteriaBuilder) ->
               criteriaBuilder.like(root.get(SampleEntity_.SAMPLE_NAME), '%' + name + '%');
    }

    public static Specification<SampleEntity> equalType(SampleType type) {
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(SampleEntity_.SAMPLE_TYPE), type);
    }

}
