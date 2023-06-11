package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.repository.BoardEntity;
import org.oopscraft.arch4j.core.role.Role;

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

    private String skin;

    @Builder.Default
    private Integer pageSize = 20;

    @Builder.Default
    private Boolean replyEnabled = true;

    @Builder.Default
    private Boolean fileEnabled = true;

    @Builder.Default
    private List<Role> accessRoles = new ArrayList<>();

    @Builder.Default
    private List<Role> readRoles = new ArrayList<>();

    @Builder.Default
    private List<Role> writeRoles = new ArrayList<>();

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
                .skin(boardEntity.getSkin())
                .pageSize(boardEntity.getPageSize())
                .replyEnabled(boardEntity.isReplyEnabled())
                .fileEnabled(boardEntity.isFileEnabled())
                .accessRoles(boardEntity.getAccessRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .readRoles(boardEntity.getReadRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .writeRoles(boardEntity.getWriteRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
