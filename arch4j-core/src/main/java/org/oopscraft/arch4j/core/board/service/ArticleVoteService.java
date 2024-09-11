package org.oopscraft.arch4j.core.board.service;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.dao.ArticleEntity;
import org.oopscraft.arch4j.core.board.dao.ArticleRepository;
import org.oopscraft.arch4j.core.board.dao.ArticleVoteEntity;
import org.oopscraft.arch4j.core.board.dao.ArticleVoteRepository;
import org.oopscraft.arch4j.core.board.model.ArticleVote;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleVoteService {

    private final ArticleRepository articleRepository;

    private final ArticleVoteRepository articleVoteRepository;

    public List<ArticleVote> getArticleVotes(String articleId) {
        return articleVoteRepository.findAllByArticleId(articleId).stream()
                .map(ArticleVote::from)
                .collect(Collectors.toList());
    }

    public Optional<ArticleVote> getArticleVote(String articleId, String userId) {
        ArticleVoteEntity.Pk pk = ArticleVoteEntity.Pk.builder()
                .articleId(articleId)
                .userId(userId)
                .build();
        return articleVoteRepository.findById(pk)
                .map(ArticleVote::from);
    }

    @Transactional
    public ArticleVote saveArticleVote(ArticleVote articleVote) {

        // get article
        ArticleEntity articleEntity = articleRepository.findById(articleVote.getArticleId()).orElseThrow();

        // update article like entity
        ArticleVoteEntity.Pk pk = ArticleVoteEntity.Pk.builder()
                .articleId(articleVote.getArticleId())
                .userId(articleVote.getUserId())
                .build();
        ArticleVoteEntity articleVoteEntity = articleVoteRepository.findById(pk).orElse(null);
        if(articleVoteEntity == null) {
            articleVoteEntity = ArticleVoteEntity.builder()
                    .articleId(articleVote.getArticleId())
                    .userId(articleVote.getUserId())
                    .build();
        }
        articleVoteEntity.setPoint(articleVote.getPoint());
        articleVoteEntity = articleVoteRepository.saveAndFlush(articleVoteEntity);

        // update article vote count
        AtomicLong votePositiveCount = new AtomicLong(0L);
        AtomicLong voteNegativeCount = new AtomicLong(0L);
        articleVoteRepository.findAllByArticleId(articleEntity.getArticleId()).forEach(element -> {
            if(element.getPoint() > 0) {
                votePositiveCount.getAndIncrement();
            }else if(element.getPoint() < 0) {
                voteNegativeCount.getAndIncrement();
            }
        });
        articleEntity.setVotePositiveCount(votePositiveCount.get());
        articleEntity.setVoteNegativeCount(voteNegativeCount.get());
        articleRepository.saveAndFlush(articleEntity);

        // return
        return ArticleVote.from(articleVoteEntity);
    }

}
