package org.oopscraft.arch4j.core.git.dao;

import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

public class GitSpecification {

    public static Specification<GitEntity> likeGitId(String gitId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(GitEntity_.GIT_ID), '%' + gitId + '%');
    }

    public static Specification<GitEntity> likeGitName(String gitName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(GitEntity_.GIT_NAME), '%' + gitName + '%');
    }

    public static Specification<GitEntity> likeUrl(String url) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(GitEntity_.URL), '%' + url + '%');
    }

}
