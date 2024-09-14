package org.oopscraft.arch4j.core.variable.dao;

import org.oopscraft.arch4j.core.variable.model.VariableSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VariableRepository extends JpaRepository<VariableEntity, String>, JpaSpecificationExecutor<VariableEntity> {

    default Page<VariableEntity> findAll(VariableSearch variableSearch, Pageable pageable) {
        Specification<VariableEntity> specification = Specification.where(null);

        if(variableSearch.getVariableId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VariableEntity_.VARIABLE_ID), '%' + variableSearch.getVariableId() + '%'));
        }
        if(variableSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(VariableEntity_.NAME), '%' + variableSearch.getName() + '%'));
        }

        return findAll(specification, pageable);
    }

}
