package org.oopscraft.arch4j.core.board.repository;

import org.oopscraft.arch4j.core.board.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    public Page<ArticleEntity> findArticles(ArticleSearch articleSearch, Pageable pageable);

}
