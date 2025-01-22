package org.chomoo.arch4j.core.board.service;

import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.board.dao.*;
import org.chomoo.arch4j.core.board.model.Article;
import org.chomoo.arch4j.core.board.model.ArticleFile;
import org.chomoo.arch4j.core.board.model.ArticleSearch;
import org.chomoo.arch4j.core.common.data.IdGenerator;
import org.chomoo.arch4j.core.common.data.ValidationUtil;
import org.chomoo.arch4j.core.common.storage.StorageClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ArticleFileRepository articleFileRepository;

    private final StorageClient fileService;

    private final PasswordEncoder passwordEncoder;

    private final ArticleVoteRepository articleVoteRepository;

    @Transactional
    public Article saveArticle(Article article, List<MultipartFile> files) {
        ValidationUtil.validate(article);
        ArticleEntity articleEntity = Optional.ofNullable(article.getArticleId())
                .flatMap(articleRepository::findById).orElse(
                        ArticleEntity.builder()
                                .articleId(IdGenerator.uuid())
                                .createdAt(LocalDateTime.now())
                                .userId(article.getUserId())
                                .userName(article.getUserName())
                                .build());
        // anonymous writer
        if(article.getUserId() == null) {
            Assert.notNull(article.getPassword(), "password is required");
            articleEntity.setPassword(passwordEncoder.encode(article.getPassword()));
        }
        articleEntity.setTitle(article.getTitle());
        articleEntity.setContentFormat(article.getContentFormat());
        articleEntity.setContent(article.getContent());
        articleEntity.setBoardId(article.getBoardId());
        articleEntity = articleRepository.saveAndFlush(articleEntity);

        // new file
        for(ArticleFile articleFile : article.getFiles()) {
            ArticleFileEntity.Pk pk = ArticleFileEntity.Pk.builder()
                    .articleId(articleEntity.getArticleId())
                    .fileId(articleFile.getFileId())
                    .build();
            ArticleFileEntity articleFileEntity = articleFileRepository.findById(pk).orElse(null);
            if(articleFileEntity == null) {
                articleFileEntity = ArticleFileEntity.builder()
                        .articleId(articleEntity.getArticleId())
                        .fileId(IdGenerator.uuid())
                        .filename(articleFile.getFilename())
                        .contentType(articleFile.getContentType())
                        .length(articleFile.getLength())
                        .build();
                articleFileRepository.saveAndFlush(articleFileEntity);

                // upload file (same filename)
                for(MultipartFile file : files) {
                    if(Objects.equals(file.getOriginalFilename(), articleFileEntity.getFilename())){
                        try {
                            fileService.upload("board", articleFileEntity.getFileId(), file.getInputStream());
                        }catch(Throwable ignore){}
                        break;
                    }
                }
            }
        }

        // deleted file
        for(ArticleFileEntity articleFileEntity : articleFileRepository.findAllByArticleIdOrderByCreatedAtAsc(articleEntity.getArticleId())) {
            if(article.getFiles().stream().noneMatch(e -> articleFileEntity.getFilename().equals(e.getFilename()))){
                articleFileRepository.delete(articleFileEntity);
                articleFileRepository.flush();

                // delete file
                fileService.delete("board", articleFileEntity.getFileId());
            }
        }

        // return saved article
        article = Article.from(articleEntity);
        article.setFiles(articleFileRepository.findAllByArticleIdOrderByCreatedAtAsc(articleEntity.getArticleId()).stream()
                .map(ArticleFile::from)
                .collect(Collectors.toList()));
        return article;
    }

    public Optional<Article> getArticle(String articleId) {
        Article article = articleRepository.findById(articleId).map(Article::from).orElseThrow();
        article.setFiles(articleFileRepository.findAllByArticleIdOrderByCreatedAtAsc(article.getArticleId()).stream()
                .map(ArticleFile::from)
                .collect(Collectors.toList()));
        return Optional.of(article);
    }

    @Transactional
    public void deleteArticle(String articleId) {
        articleFileRepository.deleteByArticleId(articleId);
        articleFileRepository.flush();
        articleRepository.deleteById(articleId);
        articleRepository.flush();
    }

    public Page<Article> getArticles(ArticleSearch articleSearch, Pageable pageable) {
        Page<ArticleEntity> articleEntityPage = articleRepository.findAll(articleSearch, pageable);
        List<Article> articles = Article.from(articleEntityPage.getContent());
        long total = articleEntityPage.getTotalElements();
        return new PageImpl<>(articles, pageable, total);
    }

    public Optional<ArticleFile> getArticleFile(String articleId, String fileId) {
        return articleFileRepository.findById(new ArticleFileEntity.Pk(articleId, fileId))
                .map(ArticleFile::from);
    }

    public InputStream getArticleFileInputStream(ArticleFile articleFile) {
        return fileService.download("board", articleFile.getFileId());
    }

}
