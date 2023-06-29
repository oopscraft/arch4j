package org.oopscraft.arch4j.core.role.dao;

import org.oopscraft.arch4j.core.user.dao.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepository extends JpaRepository<AuthorityEntity, String>, JpaSpecificationExecutor<AuthorityEntity>, UserRepositoryCustom {

}
