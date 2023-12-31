package org.oopscraft.arch4j.core.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.user.dao.UserEntity;
import org.oopscraft.arch4j.core.user.dao.UserRoleEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User {

    private String userId;

    private String userName;

    private String password;

    private UserStatus status;

    private boolean admin;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

    private LocalDateTime joinAt;

    private LocalDateTime closeAt;

    @Builder.Default
    List<Role> roles = new ArrayList<>();

    public static User from(UserEntity userEntity) {
        User user = User.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .password(userEntity.getPassword())
                .status(userEntity.getStatus())
                .admin(userEntity.isAdmin())
                .email(userEntity.getEmail())
                .mobile(userEntity.getMobile())
                .photo(userEntity.getPhoto())
                .profile(userEntity.getProfile())
                .joinAt(userEntity.getJoinAt())
                .closeAt(userEntity.getCloseAt())
                .build();

        List<Role> roles = userEntity.getUserRoleEntities().stream()
                .map(UserRoleEntity::getRoleEntity)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        user.setRoles(roles);

        return user;
    }


}
