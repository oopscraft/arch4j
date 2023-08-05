package org.oopscraft.arch4j.core.user;

import lombok.*;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.user.dao.UserEntity;
import org.oopscraft.arch4j.core.user.dao.UserRoleEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private String userId;

    private String userName;

    private String password;

    private UserType type;

    private UserStatus status;

    private String email;

    private String mobile;

    private LocalDateTime joinAt;

    private LocalDateTime loginAt;

    private String photo;

    private String profile;

    @Builder.Default
    List<Role> roles = new ArrayList<>();

}
