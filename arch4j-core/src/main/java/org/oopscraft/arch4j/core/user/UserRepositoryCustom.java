package org.oopscraft.arch4j.core.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

    Page<User> findUsers(UserSearch userSearch, Pageable pageable);

}
