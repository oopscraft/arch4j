package org.oopscraft.arch4j.core.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleEntity, String>, JpaSpecificationExecutor<ArticleEntity>, ArticleRepositoryCustom {

    @Modifying
    @Query("update ArticleEntity set commentCount = commentCount+1 where articleId=:articleId")
    void increaseCommentCount(@Param("articleId") String articleId);

    @Modifying
    @Query("update ArticleEntity set commentCount = commentCount-1 where articleId=:articleId")
    void decreaseCommentCount(@Param("articleId") String articleId);

}
