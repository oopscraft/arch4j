package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.security.SecurityPolicy;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Board {

    private String boardId;

    private String name;

    private String note;

    private String icon;

    private ContentFormat messageFormat;

    private String message;

    private String skin;

    @Builder.Default
    private Integer pageSize = 20;

    @Builder.Default
    private boolean fileEnabled = true;


    @Column(name = "access_policy", length = 16)
    @Builder.Default
    public SecurityPolicy accessPolicy = SecurityPolicy.AUTHENTICATED;

    @Builder.Default
    private List<Role> accessRoles = new ArrayList<>();

    @Column(name = "read_policy", length = 16)
    @Builder.Default
    public SecurityPolicy readPolicy = SecurityPolicy.AUTHENTICATED;

    @Builder.Default
    private List<Role> readRoles = new ArrayList<>();

    @Column(name = "write_policy", length = 16)
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

    /**
     * factory method
     * @param boardEntity board entity
     * @return board info
     */
    public static Board from(BoardEntity boardEntity) {
        return Board.builder()
                .boardId(boardEntity.getBoardId())
                .name(boardEntity.getName())
                .note(boardEntity.getNote())
                .icon(boardEntity.getIcon())
                .messageFormat(boardEntity.getMessageFormat())
                .message(boardEntity.getMessage())
                .skin(boardEntity.getSkin())
                .pageSize(boardEntity.getPageSize())
                .fileEnabled(boardEntity.isFileEnabled())
                .accessPolicy(boardEntity.getAccessPolicy())
                .accessRoles(boardEntity.getAccessRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .readPolicy(boardEntity.getReadPolicy())
                .readRoles(boardEntity.getReadRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .writePolicy(boardEntity.getWritePolicy())
                .writeRoles(boardEntity.getWriteRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .commentEnabled(boardEntity.isCommentEnabled())
                .commentPolicy(boardEntity.getCommentPolicy())
                .commentRoles(boardEntity.getCommentRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
