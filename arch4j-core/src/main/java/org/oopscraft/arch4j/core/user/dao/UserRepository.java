package org.oopscraft.arch4j.core.user.dao;

import org.oopscraft.arch4j.core.data.crpyto.CryptoUtil;
import org.oopscraft.arch4j.core.user.UserSearch;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserEntity repository
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity>, UserRepositoryCustom {

    default UserEntity findFirstByEmail(String email) {
        Specification<UserEntity> specification = Specification.where(null);
        specification = specification.and(((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(UserEntity_.EMAIL), email)));
        return findOne(specification).orElse(null);
    }

}
