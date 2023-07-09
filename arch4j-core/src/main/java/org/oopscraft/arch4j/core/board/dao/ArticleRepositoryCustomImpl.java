package org.oopscraft.arch4j.core.board.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.ArticleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ArticleEntity> findAll(ArticleSearch articleSearch, Pageable pageable) {

        // query
        QArticleEntity qArticleEntity = QArticleEntity.articleEntity;
        JPAQuery<ArticleEntity> query = jpaQueryFactory
                .select(qArticleEntity)
                .from(qArticleEntity);
        if(articleSearch.getBoardId() != null) {
            query.where(qArticleEntity.boardId.eq(articleSearch.getBoardId()));
        }
        if(articleSearch.getUserId() != null) {
            query.where(qArticleEntity.userId.eq(articleSearch.getUserId()));
        }
        if(articleSearch.getTitle() != null) {
            query.where(qArticleEntity.title.contains(articleSearch.getTitle()));
        }
        if(articleSearch.getContent() != null) {
            query.where(qArticleEntity.content.contains(articleSearch.getContent()));
        }

        // content
        List<ArticleEntity> articleEntities = query.clone()
                .orderBy(qArticleEntity.createdAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // total count
        Long total = query.clone()
                .select(qArticleEntity.count())
                .fetchOne();
        total = Optional.ofNullable(total).orElse(0L);

        // returns page
        return new PageImpl<>(articleEntities, pageable, total);
    }

}
