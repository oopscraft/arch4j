package org.oopscraft.arch4j.core.article;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.article.repository.ArticleEntity;
import org.oopscraft.arch4j.core.article.repository.ArticleRepository;
import org.oopscraft.arch4j.core.support.IdGenerator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    /**
     * saves article
     * @param article article info
     */
    public void saveArticle(Article article) {
        ArticleEntity articleEntity = articleRepository.findById(article.getId()).orElse(null);
        if(articleEntity == null) {
            articleEntity = ArticleEntity.builder()
                    .id(article.getId())
                    .build();
        }
        articleEntity.setTitle(article.getTitle());
        articleEntity.setContent(article.getContent());
        articleEntity.setBoardId(article.getBoardId());
        articleRepository.saveAndFlush(articleEntity);
    }

    /**
     * returns article info
     * @param id article id
     * @return article info
     */
    public Optional<Article> getArticle(String id) {
        return articleRepository.findById(id).map(Article::from);
    }

    /**
     * deletes article
     * @param id article id
     */
    public void deleteArticle(String id) {
        articleRepository.deleteById(id);
        articleRepository.flush();
    }

    /**
     * returns articles
     * @param articleSearch article search condition
     * @param pageable pagination info
     * @return list of article
     */
    public Page<Article> getArticles(ArticleSearch articleSearch, Pageable pageable) {
        Page<ArticleEntity> articleEntityPage = articleRepository.findArticles(articleSearch, pageable);
        List<Article> articles = articleEntityPage.getContent().stream()
                .map(Article::from)
                .collect(Collectors.toList());
        long total = articleEntityPage.getTotalElements();
        return new PageImpl<>(articles, pageable, total);
    }

}
