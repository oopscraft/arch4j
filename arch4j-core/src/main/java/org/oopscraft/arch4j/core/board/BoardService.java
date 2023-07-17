package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.board.dao.BoardRepository;
import org.oopscraft.arch4j.core.board.dao.BoardRoleEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.role.dao.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final RoleRepository roleRepository;

    private final EntityManager entityManager;

    @Transactional
    public Board saveBoard(Board board) {
        BoardEntity boardEntity = boardRepository.findById(board.getBoardId()).orElse(
                BoardEntity.builder()
                    .boardId(board.getBoardId())
                    .build());
        boardEntity.setBoardName(board.getBoardName());
        boardEntity.setNote(board.getNote());
        boardEntity.setIcon(board.getIcon());
        boardEntity.setMessageFormat(board.getMessageFormat());
        boardEntity.setMessage(board.getMessage());
        boardEntity.setSkin(board.getSkin());
        boardEntity.setPageSize(board.getPageSize());

        // access policy
        boardEntity.setAccessPolicy(board.getAccessPolicy());
        boardEntity.getAccessRoles().clear();
        board.getAccessRoles().stream()
                .map(role -> BoardRoleEntity.builder()
                        .boardId(boardEntity.getBoardId())
                        .roleId(role.getRoleId())
                        .type("ACCESS")
                        .build())
                .forEach(boardEntity.getAccessRoles()::add);

        // read policy
        boardEntity.setReadPolicy(board.getReadPolicy());
        boardEntity.getReadRoles().clear();
        board.getReadRoles().stream()
                .map(role -> BoardRoleEntity.builder()
                        .boardId(boardEntity.getBoardId())
                        .roleId(role.getRoleId())
                        .type("READ")
                        .build())
                .forEach(boardEntity.getReadRoles()::add);

        // write policy
        boardEntity.setWritePolicy(board.getWritePolicy());
        board.getWriteRoles().stream()
                .map(role -> BoardRoleEntity.builder()
                        .boardId(boardEntity.getBoardId())
                        .roleId(role.getRoleId())
                        .type("WRITE")
                        .build())
                .forEach(boardEntity.getReadRoles()::add);

        // file
        boardEntity.setFileEnabled(board.isFileEnabled());

        // comment
        boardEntity.setCommentEnabled(board.isCommentEnabled());
        boardEntity.setCommentPolicy(board.getCommentPolicy());
        boardEntity.getCommentRoles().clear();
        board.getCommentRoles().stream()
                .map(role -> BoardRoleEntity.builder()
                        .boardId(boardEntity.getBoardId())
                        .roleId(role.getRoleId())
                        .type("COMMENT")
                        .build())
                .forEach(boardEntity.getCommentRoles()::add);

        // save
        BoardEntity savedBoardEntity = boardRepository.saveAndFlush(boardEntity);
        entityManager.clear();

        // return
        return boardRepository.findById(savedBoardEntity.getBoardId())
                .map(Board::from)
                .orElseThrow();
    }

    public Optional<Board> getBoard(String boardId) {
        return boardRepository.findById(boardId)
                .map(Board::from);
    }

    public void deleteBoard(String boardId) {
        boardRepository.deleteById(boardId);
        boardRepository.flush();
    }

    public Page<Board> getBoards(BoardSearch boardSearch, Pageable pageable) {
        Page<BoardEntity> boardEntityPage = boardRepository.findAll(boardSearch, pageable);
        List<Board> boards = boardEntityPage.getContent().stream()
                .map(Board::from)
                .collect(Collectors.toList());
        long total = boardEntityPage.getTotalElements();
        return new PageImpl<>(boards, pageable, total);
    }

}
