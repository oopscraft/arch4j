package org.oopscraft.arch4j.core.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, String>, JpaSpecificationExecutor<CommentEntity> {

    List<CommentEntity> findAllByOwnerTypeAndOwnerIdOrderByCreatedAtAsc(String ownerType, String ownerId);

}
