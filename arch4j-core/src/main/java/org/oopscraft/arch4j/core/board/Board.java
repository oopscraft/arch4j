package org.oopscraft.arch4j.core.board;

import lombok.*;
import org.oopscraft.arch4j.core.board.dao.BoardEntity;
import org.oopscraft.arch4j.core.board.dao.BoardRoleEntity;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.security.SecurityPolicy;
import org.oopscraft.arch4j.core.security.SecurityUtils;

import java.util.ArrayList;
import java.util.List;
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

}
