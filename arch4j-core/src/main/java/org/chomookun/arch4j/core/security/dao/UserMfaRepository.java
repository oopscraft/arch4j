package org.chomookun.arch4j.core.security.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMfaRepository extends JpaRepository<UserMfaEntity, String> {

}
