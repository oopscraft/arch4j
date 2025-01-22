package org.chomoo.arch4j.core.board.service;

import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.board.dao.ArticleCommentEntity;
import org.chomoo.arch4j.core.board.dao.ArticleCommentRepository;
import org.chomoo.arch4j.core.board.dao.ArticleRepository;
import org.chomoo.arch4j.core.board.model.ArticleComment;
import org.chomoo.arch4j.core.common.data.IdGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;

    private final PasswordEncoder passwordEncoder;

    private final ArticleRepository articleRepository;

    @Transactional
    public ArticleComment saveArticleComment(ArticleComment articleComment) {
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleComment.getArticleId())
                .commentId(articleComment.getCommentId())
                .build();
        ArticleCommentEntity articleCommentEntity = articleCommentRepository.findById(pk).orElse(
                ArticleCommentEntity.builder()
                        .articleId(articleComment.getArticleId())
                        .commentId(IdGenerator.uuid())
                        .parentCommentId(articleComment.getParentCommentId())
                        .createdAt(LocalDateTime.now())
                        .userId(articleComment.getUserId())
                        .password(Optional.ofNullable(articleComment.getPassword())
                                .map(passwordEncoder::encode)
                                .orElse(null))
                        .build());
        articleCommentEntity.setUserName(articleComment.getUserName());
        articleCommentEntity.setContentFormat(articleComment.getContentFormat());
        articleCommentEntity.setContent(articleComment.getContent());
        articleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);
        articleRepository.increaseCommentCount(articleComment.getArticleId());
        return ArticleComment.from(articleCommentEntity);
    }

    public List<ArticleComment> getArticleComments(String articleId) {
        return articleCommentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId).stream()
                .map(ArticleComment::from)
                .collect(Collectors.toList());
    }

    public Optional<ArticleComment> getArticleComment(String articleId, String commentId) {
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleId)
                .commentId(commentId)
                .build();
        return articleCommentRepository.findById(pk)
                .map(ArticleComment::from);
    }

    @Transactional
    public void deleteArticleComment(String articleId, String commentId) {
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleId)
                .commentId(commentId)
                .build();
        ArticleCommentEntity articleCommentEntity = articleCommentRepository.findById(pk).orElseThrow();

        // check reply comment, update content
        if(articleCommentRepository.findAllByArticleIdAndParentCommentId(articleId, commentId).size() > 0) {
            articleCommentEntity.setContentFormat(ArticleComment.ContentFormat.TEXT);
            articleCommentEntity.setContent("- This comment has bean deleted. -");
            articleCommentRepository.saveAndFlush(articleCommentEntity);

        }
        // no reply comments, delete
        else{
            articleCommentRepository.delete(articleCommentEntity);
            articleCommentRepository.flush();
            articleRepository.decreaseCommentCount(articleId);
        }

    }

}
