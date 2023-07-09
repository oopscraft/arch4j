package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.board.dao.BoardRepository;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.security.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

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
        boardEntity.setFileEnabled(board.isFileEnabled());
        boardEntity.setAccessPolicy(board.getAccessPolicy());
        boardEntity.setAccessRoles(board.getAccessRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .build())
                .collect(Collectors.toList()));
        boardEntity.setReadPolicy(board.getReadPolicy());
        boardEntity.setReadRoles(board.getReadRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .build())
                .collect(Collectors.toList()));
        boardEntity.setWritePolicy(board.getWritePolicy());
        boardEntity.setWriteRoles(board.getWriteRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .build())
                .collect(Collectors.toList()));
        boardEntity.setCommentEnabled(board.isCommentEnabled());
        boardEntity.setCommentPolicy(board.getCommentPolicy());
        boardEntity.setCommentRoles(board.getCommentRoles().stream()
                .map(role -> RoleEntity.builder()
                        .roleId(role.getRoleId())
                        .build())
                .collect(Collectors.toList()));

        // save
        boardEntity = boardRepository.saveAndFlush(boardEntity);
        return Board.from(boardEntity);
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

    public void checkAccessPermission(Board board) {
        SecurityUtils.checkPermission(board.getAccessPolicy(), board.getAccessRoles());
    }

    public void checkReadPermission(Board board) {
        SecurityUtils.checkPermission(board.getReadPolicy(), board.getReadRoles());
    }

    public void checkWritePermission(Board board) {
        SecurityUtils.checkPermission(board.getWritePolicy(), board.getWriteRoles());
    }

    public void checkCommentPermission(Board board) {
        SecurityUtils.checkPermission(board.getCommentPolicy(), board.getCommentRoles());
    }

}
