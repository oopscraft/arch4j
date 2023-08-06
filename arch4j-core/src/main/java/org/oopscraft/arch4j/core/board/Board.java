package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.board.dao.BoardRoleEntity;
import org.oopscraft.arch4j.core.user.Role;
import org.oopscraft.arch4j.core.security.SecurityPolicy;
import org.oopscraft.arch4j.core.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {

    private String boardId;

    private String boardName;

    private String note;

    private String icon;

    private MessageFormat messageFormat;

    private String message;

    private String skin;

    @Builder.Default
    private Integer pageSize = 20;

    @Builder.Default
    private boolean fileEnabled = true;

    @Builder.Default
    public SecurityPolicy readPolicy = SecurityPolicy.AUTHENTICATED;

    @Builder.Default
    private List<Role> readRoles = new ArrayList<>();

    @Builder.Default
    public SecurityPolicy writePolicy = SecurityPolicy.AUTHENTICATED;

    @Builder.Default
    private List<Role> writeRoles = new ArrayList<>();

    @Builder.Default
    private boolean commentEnabled = true;

    @Builder.Default
    private SecurityPolicy commentPolicy = SecurityPolicy.AUTHENTICATED;

    @Builder.Default
    private List<Role> commentRoles = new ArrayList<>();

    public boolean hasReadPermission() {
        return SecurityUtils.hasPermission(readPolicy, readRoles);
    }

    public boolean hasWritePermission() {
        return SecurityUtils.hasPermission(writePolicy, writeRoles);
    }

    public boolean hasCommentPermission() {
        return SecurityUtils.hasPermission(commentPolicy, commentRoles);
    }

    public static Board from(BoardEntity boardEntity) {
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
        List<Role> readRoles = boardEntity.getReadBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setReadRoles(readRoles);

        // write policy
        board.setWritePolicy(boardEntity.getWritePolicy());
        List<Role> writeRoles = boardEntity.getWriteBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setWriteRoles(writeRoles);

        // comment policy
        board.setCommentPolicy(boardEntity.getCommentPolicy());
        List<Role> commentRoles = boardEntity.getCommentBoardRoleEntities().stream()
                .map(BoardRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        board.setCommentRoles(commentRoles);

        // return
        return board;
    }

}
