package org.oopscraft.arch4j.core.article.repository;

import org.oopscraft.arch4j.core.article.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleRepositoryCustom {

    public Page<ArticleEntity> findArticles(ArticleSearch articleSearch, Pageable pageable);

}
