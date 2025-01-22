package org.chomookun.arch4j.core.security.dao;

import org.chomookun.arch4j.core.security.model.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<UserEntity> findAll(UserSearch userSearch, Pageable pageable);

}
