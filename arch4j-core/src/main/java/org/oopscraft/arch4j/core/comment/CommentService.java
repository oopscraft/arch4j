package org.oopscraft.arch4j.core.comment;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.comment.repository.CommentEntity;
import org.oopscraft.arch4j.core.comment.repository.CommentRepository;
import org.oopscraft.arch4j.core.support.IdGenerator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * save comment
     * @param targetType target type
     * @param targetId target id
     * @param comment comment info
     */
    public void saveComment(TargetType targetType, String targetId, Comment comment) {
        CommentEntity commentEntity;
        if(comment.getId() == null) {
            commentEntity = CommentEntity.builder()
                    .id(IdGenerator.uuid())
                    .targetType(targetType)
                    .targetId(targetId)
                    .build();
        }else{
            commentEntity = commentRepository.findById(comment.getId())
                    .orElseThrow(()->new NullPointerException("comment not found"));
        }
        commentEntity.setContent(comment.getContent());
        commentEntity.setUserId(comment.getUserId());
        commentRepository.saveAndFlush(commentEntity);
    }

    /**
     * get comments
     * @param target target
     * @param targetId target id
     * @return comment list
     */
    public List<Comment> getComments(TargetType target, String targetId) {
        return commentRepository.findAllByTargetTypeAndTargetId(target, targetId).stream()
                .map(Comment::from)
                .collect(Collectors.toList());
    }

}
