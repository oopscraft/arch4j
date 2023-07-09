package org.oopscraft.arch4j.core.user.dao;

import org.oopscraft.arch4j.core.user.UserLoginSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, UserLoginEntity.Pk>, JpaSpecificationExecutor<UserLoginEntity> {

    default Page<UserLoginEntity> findAll(UserLoginSearch userLoginSearch, Pageable pageable) {
        Specification<UserLoginEntity> specification = (root, query, criteriaBuilder) -> null;
        if(userLoginSearch.getUserId() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(UserLoginEntity_.USER_ID), '%' + userLoginSearch.getUserId() + '%'));
        }
        if(userLoginSearch.getIpAddress() != null) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(UserLoginEntity_.IP_ADDRESS), '%' + userLoginSearch.getIpAddress() + '%'));
        }
        return findAll(specification, pageable);
    }

}
