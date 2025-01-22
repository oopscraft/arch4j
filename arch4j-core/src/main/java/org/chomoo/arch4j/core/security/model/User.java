package org.chomoo.arch4j.core.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseModel;
import org.chomoo.arch4j.core.security.dao.UserEntity;
import org.chomoo.arch4j.core.security.dao.UserRoleEntity;

import java.time.Instant;
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

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String name;

    private boolean admin;

    private Status status;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

    private Instant joinAt;

    private Instant closeAt;

    private Instant passwordAt;

    private Instant expireAt;

    @Builder.Default
    List<Role> roles = new ArrayList<>();

    public enum Status { ACTIVE, LOCKED, CLOSED }

    /**
     * user factory method
     * @param userEntity user entity
     * @return user
     */
    public static User from(UserEntity userEntity) {
        User user = User.builder()
                .systemRequired(userEntity.isSystemRequired())
                .systemUpdatedAt(userEntity.getSystemUpdatedAt())
                .systemUpdatedBy(userEntity.getSystemUpdatedBy())
                .userId(userEntity.getUserId())
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .name(userEntity.getName())
                .status(userEntity.getStatus())
                .admin(userEntity.isAdmin())
                .email(userEntity.getEmail())
                .mobile(userEntity.getMobile())
                .photo(userEntity.getPhoto())
                .profile(userEntity.getProfile())
                .build();

        // roles
        List<Role> roles = userEntity.getUserRoles().stream()
                .map(UserRoleEntity::getRole)
                .filter(Objects::nonNull)
                .map(Role::from)
                .collect(Collectors.toList());
        user.setRoles(roles);

        // return
        return user;
    }


}
