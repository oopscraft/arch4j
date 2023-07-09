package org.oopscraft.arch4j.core.user.dao;

import org.oopscraft.arch4j.core.user.UserSearch;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserEntity repository
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity>, UserRepositoryCustom {

//    default Page<UserEntity> findAll(UserSearch userSearch, Pageable pageable) {
//
//
//    }

    UserEntity findFirstByEmail(String email);

}
