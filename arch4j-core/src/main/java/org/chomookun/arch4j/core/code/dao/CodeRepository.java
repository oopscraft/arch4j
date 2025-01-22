package org.chomookun.arch4j.core.code.dao;

import org.chomookun.arch4j.core.code.model.CodeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository 
public interface CodeRepository extends JpaRepository<CodeEntity,String>, JpaSpecificationExecutor<CodeEntity> {

    default Page<CodeEntity> findAll(CodeSearch codeSearch, Pageable pageable) {
        Specification<CodeEntity> specification = Specification.where(null);

        if(codeSearch.getCodeId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(CodeEntity_.CODE_ID), '%' + codeSearch.getCodeId() + '%'));
        }
        if(codeSearch.getName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(CodeEntity_.NAME), '%' + codeSearch.getName() + '%'));
        }

        return findAll(specification, pageable);
    }

}
