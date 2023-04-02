package org.oopscraft.arch4j.core.user.repository;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * UserEntity repository
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity>, UserRepositorySupport {

    public UserEntity findByUsername(String username);

    public UserEntity findByEmail(String email);

    public UserEntity findByMobile(String mobile);

}
