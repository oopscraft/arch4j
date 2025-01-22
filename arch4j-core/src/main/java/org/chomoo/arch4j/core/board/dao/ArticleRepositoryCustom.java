package org.chomoo.arch4j.core.board.dao;

import org.chomoo.arch4j.core.board.model.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    Page<ArticleEntity> findAll(ArticleSearch articleSearch, Pageable pageable);

}
