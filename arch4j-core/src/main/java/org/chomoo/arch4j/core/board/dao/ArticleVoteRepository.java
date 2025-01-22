package org.chomoo.arch4j.core.board.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleVoteRepository extends JpaRepository<ArticleVoteEntity, ArticleVoteEntity.Pk> {

    List<ArticleVoteEntity> findAllByArticleId(String articleId);

}
