package org.oopscraft.arch4j.core.code;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CodeRepositorySupport {

    Page<Code> findCodes(CodeSearch codeSearch, Pageable pageable);

}
