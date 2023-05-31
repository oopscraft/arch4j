package org.oopscraft.arch4j.core.file.repository;

import org.oopscraft.arch4j.core.board.repository.ArticleEntity;
import org.oopscraft.arch4j.core.board.repository.ArticleRepositoryCustom;
import org.oopscraft.arch4j.core.file.FileInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfoEntity, String>, JpaSpecificationExecutor<FileInfoEntity> {

}
