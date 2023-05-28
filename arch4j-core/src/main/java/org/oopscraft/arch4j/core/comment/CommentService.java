package org.oopscraft.arch4j.core.comment;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.comment.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    /**
     * get comments
     * @param target target
     * @param targetId target id
     * @return comment list
     */
    public List<Comment> getComments(CommentTarget target, String targetId) {
        return commentRepository.findAllByTargetAndTargetId(target, targetId).stream()
                .map(Comment::from)
                .collect(Collectors.toList());
    }

}
