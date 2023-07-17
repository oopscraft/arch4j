package org.oopscraft.arch4j.core.user.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.converter.CryptoConverter;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.user.UserStatus;
import org.oopscraft.arch4j.core.user.UserType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "core_user",
    indexes = {
        @Index(name = "ix_name", columnList = "user_name"),
        @Index(name = "ix_email", columnList = "email"),
        @Index(name = "ix_mobile", columnList = "mobile")
    }
)
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends SystemFieldEntity {

    @Id
    @Column(name = "user_id", length = 32)
    private String userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "type", length = 16)
    @Builder.Default
    private UserType type = UserType.GENERAL;

    @Column(name = "status", length = 16)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "email", length = 1024)
    @Convert(converter = CryptoConverter.class)
    private String email;

    @Column(name = "mobile", length = 1024)
    @Convert(converter = CryptoConverter.class)
    private String mobile;

    @Column(name = "join_at")
    private LocalDateTime joinAt;

    @Column(name = "close_at")
    private LocalDateTime closeAt;

    @Column(name = "photo")
    @Lob
    private String photo;

    @Column(name = "profile")
    @Lob
    private String profile;

    @OneToMany(mappedBy = UserRoleEntity_.USER_ID, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<UserRoleEntity> roles = new ArrayList<>();

}
