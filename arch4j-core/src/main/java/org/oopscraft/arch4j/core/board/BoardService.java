package org.oopscraft.arch4j.core.board;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.board.dao.BoardRepository;
import org.oopscraft.arch4j.core.board.dao.BoardRoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional
    public Board saveBoard(Board board) {
        BoardEntity boardEntity = boardRepository.findById(board.getBoardId())
                .orElse(BoardEntity.builder()
                    .boardId(board.getBoardId())
                    .build());
        boardEntity.setSystemUpdatedAt(LocalDateTime.now());    // disable dirty checking
        boardEntity.setBoardName(board.getBoardName());
        boardEntity.setIcon(board.getIcon());
        boardEntity.setMessageFormat(board.getMessageFormat());
        boardEntity.setMessage(board.getMessage());
        boardEntity.setSkin(board.getSkin());
        boardEntity.setPageSize(board.getPageSize());

        // access policy
        boardEntity.getAccessBoardRoleEntities().clear();
        board.getAccessRoles().forEach(accessRole -> {
            BoardRoleEntity boardRoleEntity = BoardRoleEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .roleId(accessRole.getRoleId())
                    .type("ACCESS")
                    .build();
            boardEntity.getAccessBoardRoleEntities().add(boardRoleEntity);
        });

        // read policy
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
        boardEntity.getWriteBoardRoleEntities().clear();
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
        boardEntity.setFileSizeLimit(board.getFileSizeLimit());
        boardEntity.getFileBoardRoleEntities().clear();
        board.getFileRoles().forEach(fileRole -> {
            BoardRoleEntity boardRoleEntity = BoardRoleEntity.builder()
                    .boardId(boardEntity.getBoardId())
                    .roleId(fileRole.getRoleId())
                    .type("FILE")
                    .build();
            boardEntity.getFileBoardRoleEntities().add(boardRoleEntity);
        });

        // comment
        boardEntity.setCommentEnabled(board.isCommentEnabled());
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
