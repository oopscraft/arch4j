package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.repository.*;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.data.ValidationUtils;
import org.oopscraft.arch4j.core.file.FileService;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;

    private final PasswordEncoder passwordEncoder;

    private final ArticleRepository articleRepository;

    /**
     * save article comment
     * @param articleComment article comment
     * @return article comment
     */
    public ArticleComment saveArticleComment(ArticleComment articleComment) {
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleComment.getArticleId())
                .commentId(articleComment.getCommentId())
                .build();
        ArticleCommentEntity articleCommentEntity = articleCommentRepository.findById(pk).orElse(null);

        // create new article comment
        if(articleCommentEntity == null) {
            articleCommentEntity = ArticleCommentEntity.builder()
                    .articleId(articleComment.getArticleId())
                    .commentId(IdGenerator.uuid())
                    .parentCommentId(articleComment.getParentCommentId())
                    .createdAt(LocalDateTime.now())
                    .userId(articleComment.getUserId())
                    .password(Optional.ofNullable(articleComment.getPassword())
                            .map(passwordEncoder::encode)
                            .orElse(null))
                    .build();
        }

        // save
        articleCommentEntity.setUserName(articleComment.getUserName());
        articleCommentEntity.setContentFormat(articleComment.getContentFormat());
        articleCommentEntity.setContent(articleComment.getContent());
        articleCommentEntity = articleCommentRepository.saveAndFlush(articleCommentEntity);

        // increase article comment count
        articleRepository.increaseCommentCount(articleComment.getArticleId());

        // return
        return ArticleComment.from(articleCommentEntity);
    }

    /**
     * return article comments by article id
     * @param articleId article id
     * @return article comment list
     */
    public List<ArticleComment> getArticleComments(String articleId) {
        return articleCommentRepository.findAllByArticleIdOrderByCreatedAtAsc(articleId).stream()
                .map(ArticleComment::from)
                .collect(Collectors.toList());
    }

    /**
     * return article comment
     * @param articleId article id
     * @param commentId comment id
     * @return article comment
     */
    public Optional<ArticleComment> getArticleComment(String articleId, String commentId) {
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleId)
                .commentId(commentId)
                .build();
        return articleCommentRepository.findById(pk)
                .map(ArticleComment::from);
    }

    /**
     * delete article comment
     * @param articleId article id
     * @param commentId comment id
     */
    public void deleteArticleComment(String articleId, String commentId) {

        // delete article comment
        ArticleCommentEntity.Pk pk = ArticleCommentEntity.Pk.builder()
                .articleId(articleId)
                .commentId(commentId)
                .build();
        articleCommentRepository.deleteById(pk);
        articleCommentRepository.flush();

        // decrease article comment count
        articleRepository.decreaseCommentCount(articleId);
    }

}
