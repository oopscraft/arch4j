package org.oopscraft.arch4j.core.git.dao;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.oopscraft.arch4j.core.code.dao.CodeEntity;
import org.oopscraft.arch4j.core.git.GitSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GitRepository extends JpaRepository<GitEntity, String>, JpaSpecificationExecutor<GitEntity>, QuerydslPredicateExecutor<GitEntity> {

    default Page<GitEntity> findAll(GitSearch gitSearch, Pageable pageable) {
        Specification<GitEntity> specification = (root, query, criteriaBuilder) -> null;
        if(gitSearch.getGitId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(GitEntity_.GIT_ID), '%' + gitSearch.getGitId() + '%'));
        }
        if(gitSearch.getGitName() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(GitEntity_.GIT_NAME), '%' + gitSearch.getGitName() + '%'));
        }
        return findAll(specification, pageable);
    }

}
