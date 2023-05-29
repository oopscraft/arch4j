package org.oopscraft.arch4j.core.comment.repository;

import org.oopscraft.arch4j.core.comment.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String>, JpaSpecificationExecutor<CommentEntity> {

    public List<CommentEntity> findAllByTargetTypeAndTargetId(TargetType targetType, String targetId);

}
