package org.oopscraft.arch4j.core.comment;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.comment.repository.CommentEntity;
import org.oopscraft.arch4j.core.comment.repository.CommentRepository;
import org.oopscraft.arch4j.core.data.IdGenerator;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * save comment
     * @param ownerType target type
     * @param ownerId target id
     * @param comment comment info
     * @return comment
     */
    public Comment saveComment(String ownerType, String ownerId, Comment comment) {
        CommentEntity commentEntity;

        // create new comment
        if(comment.getId() == null) {
            commentEntity = CommentEntity.builder()
                    .id(IdGenerator.uuid())
                    .ownerType(ownerType)
                    .ownerId(ownerId)
                    .createdAt(LocalDateTime.now())
                    .parentId(comment.getParentId())
                    .build();

            // authenticated user
            if(SecurityUtils.isAuthenticated()) {
                commentEntity.setUserId(SecurityUtils.getCurrentUserId());
            }
            // anonymous user
            else{
                // userName required
                if(comment.getUserName() == null) {
                    throw new RuntimeException("userName is required");
                }
                commentEntity.setUserName(comment.getUserName());

                // password required
                if(comment.getPassword() == null) {
                    throw new RuntimeException("password is required");
                }
                commentEntity.setPassword(comment.getPassword());
            }
        }
        // modify previous comment
        else{
            commentEntity = commentRepository.findById(comment.getId())
                    .orElseThrow(()->new NullPointerException("comment not found"));

            // check current user is match to writer
            if(commentEntity.getUserId() != null) {
                if(!commentEntity.getUserId().equals(SecurityUtils.getCurrentUserId())){
                    throw new RuntimeException("not matches user id");
                }
            }
            // if anonymous user's article, check password
            else{
                if(!passwordEncoder.matches(comment.getPassword(), commentEntity.getPassword())) {
                    throw new RuntimeException("password not matches");
                }
            }
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
        return commentRepository.findAllByOwnerTypeAndOwnerIdOrderByCreatedAtAsc(ownerType, ownerId).stream()
                .map(Comment::from)
                .collect(Collectors.toList());
    }

    /**
     * get comment
     * @param id comment id
     * @return comment info
     */
    public Optional<Comment> getComment(String id) {
        return commentRepository.findById(id).map(Comment::from);
    }

    /**
     * delete comment
     * @param id comment id
     */
    public void deleteComment(String id) {
        commentRepository.deleteById(id);
    }

}
