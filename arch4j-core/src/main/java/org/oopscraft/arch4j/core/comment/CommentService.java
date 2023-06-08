package org.oopscraft.arch4j.core.comment;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.comment.repository.CommentEntity;
import org.oopscraft.arch4j.core.comment.repository.CommentRepository;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * save comment
     * @param ownerType target type
     * @param ownerId target id
     * @param comment comment info
     * @return comment
     */
    public Comment saveComment(String ownerType, String ownerId, Comment comment) {
        CommentEntity commentEntity;
        if(comment.getId() == null) {
            commentEntity = CommentEntity.builder()
                    .id(IdGenerator.uuid())
                    .ownerType(ownerType)
                    .ownerId(ownerId)
                    .createdAt(LocalDateTime.now())
                    .parentId(comment.getParentId())
                    .build();
        }else{
            commentEntity = commentRepository.findById(comment.getId())
                    .orElseThrow(()->new NullPointerException("comment not found"));
        }
        commentEntity.setContent(comment.getContent());
        commentEntity.setUserId(comment.getUserId());
        commentEntity = commentRepository.saveAndFlush(commentEntity);
        return Comment.from(commentEntity);
    }

    /**
     * get comments
     * @param ownerType target
     * @param ownerId target id
     * @return comment list
     */
    public List<Comment> getComments(String ownerType, String ownerId) {
        return commentRepository.findAllByOwnerTypeAndOwnerIdOrderByCreatedAtDesc(ownerType, ownerId).stream()
                .map(Comment::from)
                .collect(Collectors.toList());
    }

}
