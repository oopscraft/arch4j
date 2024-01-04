package org.oopscraft.arch4j.core.user.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseEntity;
import org.oopscraft.arch4j.core.data.converter.BooleanToYNConverter;
import org.oopscraft.arch4j.core.data.converter.CryptoConverter;
import org.oopscraft.arch4j.core.user.UserStatus;

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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity {

    @Id
    @Column(name = "user_id", length = 32)
    @Setter(AccessLevel.PRIVATE)
    private String userId;

    @Column(name = "user_name", length = 128)
    private String userName;

    @Column(name = "password", length = 256)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "status", length = 16)
    private UserStatus status;

    @Column(name = "admin", length = 1)
    @Convert(converter = BooleanToYNConverter.class)
    private boolean admin;

    @Column(name = "email", length = 128)
    @Convert(converter = CryptoConverter.class)
    private String email;

    @Column(name = "mobile", length = 64)
    @Convert(converter = CryptoConverter.class)
    private String mobile;

    @Column(name = "photo")
    @Lob
    private String photo;

    @Column(name = "profile")
    @Lob
    private String profile;

    @Column(name = "join_at")
    private LocalDateTime joinAt;

    @Column(name = "close_at")
    private LocalDateTime closeAt;

    @Column(name = "password_at")
    private LocalDateTime passwordAt;

    @Column(name = "expire_at")
    private LocalDateTime expireAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", updatable = false)
    @Builder.Default
    private List<UserRoleEntity> userRoles = new ArrayList<>();

}
