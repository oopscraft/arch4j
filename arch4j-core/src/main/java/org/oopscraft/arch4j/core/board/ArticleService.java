package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleFileEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleFileRepository;
import org.oopscraft.arch4j.core.board.repository.ArticleRepository;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.data.ValidationUtils;
import org.oopscraft.arch4j.core.file.FileService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final ArticleFileRepository articleFileRepository;

    private final FileService fileService;

    /**
     * saves article
     * @param article article info
     * @return article
     */
    public Article saveArticle(Article article, MultipartFile[] files) {

        // validate
        ValidationUtils.validate(article);

        // find previous article entity
        ArticleEntity articleEntity = Optional.ofNullable(article.getArticleId())
                .flatMap(articleRepository::findById)
                .orElse(null);

        // create new article
        if(articleEntity == null) {
            articleEntity = ArticleEntity.builder()
                    .articleId(IdGenerator.uuid())
                    .createdAt(LocalDateTime.now())
                    .userId(article.getUserId())
                    .userName(article.getUserName())
                    .password(article.getPassword())
                    .build();
        }

        // set article property
        articleEntity.setTitle(article.getTitle());
        articleEntity.setContent(article.getContent());
        articleEntity.setBoardId(article.getBoardId());

        // save article entity
        articleEntity = articleRepository.saveAndFlush(articleEntity);

        // files (new file)
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

        // file (deleted file)
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

    /**
     * returns article info
     * @param articleId article id
     * @return article info
     */
    public Optional<Article> getArticle(String articleId) {
        Article article = articleRepository.findById(articleId).map(Article::from).orElseThrow();
        article.setFiles(articleFileRepository.findAllByArticleIdOrderByCreatedAtAsc(article.getArticleId()).stream()
                .map(ArticleFile::from)
                .collect(Collectors.toList()));
        return Optional.of(article);
    }

    /**
     * deletes article
     * @param articleId article id
     */
    public void deleteArticle(String articleId) {
        articleFileRepository.deleteByArticleId(articleId);
        articleFileRepository.flush();
        articleRepository.deleteById(articleId);
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

    /**
     * return article file
     * @param articleId article id
     * @param fileId file id
     * @return article file info
     */
    public Optional<ArticleFile> getArticleFile(String articleId, String fileId) {
        return articleFileRepository.findById(new ArticleFileEntity.Pk(articleId, fileId))
                .map(ArticleFile::from);
    }

    /**
     * get article file input stream
     * @param articleFile article file
     * @return article file input stream
     */
    public InputStream getArticleFileInputStream(ArticleFile articleFile) {
        return fileService.download("board", articleFile.getFileId());
    }

}
