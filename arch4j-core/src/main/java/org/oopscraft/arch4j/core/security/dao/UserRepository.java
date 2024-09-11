package org.oopscraft.arch4j.core.security.dao;

import org.oopscraft.arch4j.core.security.dao.UserEntity_;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity>, UserRepositoryCustom {

    Optional<UserEntity> findByUsername(String username);

}
