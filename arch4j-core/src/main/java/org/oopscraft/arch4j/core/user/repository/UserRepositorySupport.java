package org.oopscraft.arch4j.core.user.repository;

import org.oopscraft.arch4j.core.user.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositorySupport {

    Page<UserEntity> findUsers(UserSearch userSearch, Pageable pageable);

}
