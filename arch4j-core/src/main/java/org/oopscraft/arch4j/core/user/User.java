package org.oopscraft.arch4j.core.user;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.user.repository.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private String id;

    private String password;

    private String name;

    @Builder.Default
    private UserType type = UserType.GENERAL;

    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    private String email;

    private String mobile;

    private LocalDateTime joinDateTime;

    private LocalDateTime loginDateTime;

    private String photo;

    private String profile;

    @Builder.Default
    List<Role> roles = new ArrayList<>();

    public static User from(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .password(userEntity.getPassword())
                .type(userEntity.getType())
                .status(userEntity.getStatus())
                .email(userEntity.getEmail())
                .mobile(userEntity.getMobile())
                .joinDateTime(userEntity.getJoinDateTime())
                .loginDateTime(userEntity.getLoginDateTime())
                .photo(userEntity.getPhoto())
                .profile(userEntity.getProfile())
                .roles(userEntity.getRoles().stream()
                        .map(Role::from)
                        .collect(Collectors.toList()))
                .build();
    }

}
