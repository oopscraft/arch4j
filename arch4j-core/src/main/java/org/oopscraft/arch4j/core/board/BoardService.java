package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.board.dao.BoardRepository;
import org.oopscraft.arch4j.core.board.dao.BoardRoleEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    private final RoleService roleService;

    @Transactional
    public Board saveBoard(Board board) {
        BoardEntity boardEntity = boardRepository.findById(board.getBoardId())
                .orElse(BoardEntity.builder()
                    .boardId(board.getBoardId())
                    .build());
        boardEntity.setBoardName(board.getBoardName());
        boardEntity.setNote(board.getNote());
        boardEntity.setIcon(board.getIcon());
        boardEntity.setMessageFormat(board.getMessageFormat());
        boardEntity.setMessage(board.getMessage());
        boardEntity.setSkin(board.getSkin());
        boardEntity.setPageSize(board.getPageSize());

        // read policy
        boardEntity.setReadPolicy(board.getReadPolicy());
        boardEntity.getReadBoardRoleEntities().clear();
        board.getReadRoles().forEach(readRole -> {
            BoardRoleEntity boardRoleEntity = BoardRoleEntity.builder()
                            .boardId(boardEntity.getBoardId())
                            .roleId(readRole.getRoleId())
                            .type("READ")
                            .build();
            boardEntity.getReadBoardRoleEntities().add(boardRoleEntity);
        });

        // write policy
        boardEntity.setWritePolicy(board.getWritePolicy());
        board.getWriteRoles().forEach(writeRole -> {
            BoardRoleEntity boardRoleEntity = BoardRoleEntity.builder()
                        .boardId(boardEntity.getBoardId())
                        .roleId(writeRole.getRoleId())
                        .type("WRITE")
                        .build();
            boardEntity.getWriteBoardRoleEntities().add(boardRoleEntity);
        });

        // file
        boardEntity.setFileEnabled(board.isFileEnabled());

        // comment
        boardEntity.setCommentEnabled(board.isCommentEnabled());
        boardEntity.setCommentPolicy(board.getCommentPolicy());
        boardEntity.getCommentBoardRoleEntities().clear();
        board.getCommentRoles().forEach(commentRole -> {
           BoardRoleEntity boardRoleEntity = BoardRoleEntity.builder()
                        .boardId(boardEntity.getBoardId())
                        .roleId(commentRole.getRoleId())
                        .type("COMMENT")
                        .build();
           boardEntity.getCommentBoardRoleEntities().add(boardRoleEntity);
        });

        // save
        BoardEntity savedBoardEntity = boardRepository.saveAndFlush(boardEntity);

        // return
        return this.getBoard(savedBoardEntity.getBoardId())
                .orElseThrow();
    }

    public Optional<Board> getBoard(String boardId) {
        return boardRepository.findById(boardId)
                .map(this::mapToBoard);
    }

    public Board mapToBoard(BoardEntity boardEntity) {
        Board board = Board.builder()
                .boardId(boardEntity.getBoardId())
                .boardName(boardEntity.getBoardName())
                .note(boardEntity.getNote())
                .icon(boardEntity.getIcon())
                .messageFormat(boardEntity.getMessageFormat())
                .message(boardEntity.getMessage())
                .skin(boardEntity.getSkin())
                .pageSize(boardEntity.getPageSize())
                .fileEnabled(boardEntity.isFileEnabled())
                .build();

        // read policy
        board.setReadPolicy(boardEntity.getReadPolicy());
        boardEntity.getReadBoardRoleEntities().forEach(boardRoleEntity -> {
            Role role = roleService.getRole(boardRoleEntity.getRoleId())
                    .orElse(Role.builder()
                            .roleId(boardRoleEntity.getRoleId())
                            .build());
            board.getReadRoles().add(role);
        });

        // write policy
        board.setWritePolicy(boardEntity.getWritePolicy());
        boardEntity.getWriteBoardRoleEntities().forEach(boardRoleEntity -> {
            Role role = roleService.getRole(boardRoleEntity.getRoleId())
                    .orElse(Role.builder()
                            .roleId(boardRoleEntity.getRoleId())
                            .build());
            board.getWriteRoles().add(role);
        });

        // comment policy
        board.setCommentPolicy(boardEntity.getCommentPolicy());
        boardEntity.getCommentBoardRoleEntities().forEach(boardRoleEntity -> {
            Role role = roleService.getRole(boardRoleEntity.getRoleId())
                    .orElse(Role.builder()
                            .roleId(boardRoleEntity.getRoleId())
                            .build());
            board.getCommentRoles().add(role);
        });

        return board;
    }

    public void deleteBoard(String boardId) {
        boardRepository.deleteById(boardId);
        boardRepository.flush();
    }

    public Page<Board> getBoards(BoardSearch boardSearch, Pageable pageable) {
        Page<BoardEntity> boardEntityPage = boardRepository.findAll(boardSearch, pageable);
        List<Board> boards = boardEntityPage.getContent().stream()
                .map(this::mapToBoard)
                .collect(Collectors.toList());
        long total = boardEntityPage.getTotalElements();
        return new PageImpl<>(boards, pageable, total);
    }

}
