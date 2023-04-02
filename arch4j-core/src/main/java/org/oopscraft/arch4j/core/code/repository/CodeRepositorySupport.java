package org.oopscraft.arch4j.core.code.repository;

import org.oopscraft.arch4j.core.code.CodeSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CodeRepositorySupport {

    Page<CodeEntity> findCodes(CodeSearch codeSearch, Pageable pageable);

}
