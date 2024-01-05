package org.oopscraft.arch4j.core.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseModel;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.user.dao.UserEntity;
import org.oopscraft.arch4j.core.user.dao.UserRoleEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class User extends BaseModel {

    private String userId;

    private String userName;

    private UserStatus userStatus;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private boolean admin;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

    private LocalDateTime joinAt;

    private LocalDateTime closeAt;

    private LocalDateTime passwordAt;

    private LocalDateTime expireAt;

    @Builder.Default
    List<Role> roles = new ArrayList<>();

    public static User from(UserEntity userEntity) {
        User user = User.builder()
                .systemRequired(userEntity.isSystemRequired())
                .systemUpdatedAt(userEntity.getSystemUpdatedAt())
                .systemUpdatedBy(userEntity.getSystemUpdatedBy())
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .password(userEntity.getPassword())
                .userStatus(userEntity.getUserStatus())
                .admin(userEntity.isAdmin())
                .email(userEntity.getEmail())
                .mobile(userEntity.getMobile())
                .photo(userEntity.getPhoto())
                .profile(userEntity.getProfile())
                .joinAt(userEntity.getJoinAt())
                .passwordAt(userEntity.getPasswordAt())
                .expireAt(userEntity.getExpireAt())
                .closeAt(userEntity.getCloseAt())
                .build();

        List<Role> roles = userEntity.getUserRoles().stream()
                .map(UserRoleEntity::getRole)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        user.setRoles(roles);

        return user;
    }


}
