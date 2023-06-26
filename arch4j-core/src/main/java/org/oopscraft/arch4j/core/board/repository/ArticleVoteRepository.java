package org.oopscraft.arch4j.core.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleVoteRepository extends JpaRepository<ArticleVoteEntity, ArticleVoteEntity.Pk> {

    List<ArticleVoteEntity> findAllByArticleId(String articleId);

}
