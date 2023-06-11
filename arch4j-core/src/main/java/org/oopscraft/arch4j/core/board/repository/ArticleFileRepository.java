package org.oopscraft.arch4j.core.board.repository;

import org.oopscraft.arch4j.core.board.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleFileRepository extends JpaRepository<ArticleFileEntity, ArticleFileEntity.Pk> {

    List<ArticleFileEntity> findAllByArticleIdOrderByCreatedAtAsc(String articleId);

}
