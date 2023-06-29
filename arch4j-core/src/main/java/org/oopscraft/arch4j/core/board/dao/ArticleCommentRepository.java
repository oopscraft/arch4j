package org.oopscraft.arch4j.core.board.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleCommentEntity, ArticleCommentEntity.Pk> {

    List<ArticleCommentEntity> findAllByArticleIdOrderByCreatedAtAsc(String articleId);

}
