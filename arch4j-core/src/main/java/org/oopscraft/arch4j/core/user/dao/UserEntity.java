package org.oopscraft.arch4j.core.user.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.converter.CryptoConverter;
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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserEntity extends SystemFieldEntity {

    @Id
    @Column(name = "user_id", length = 32)
    @Setter(AccessLevel.PRIVATE)
    private String userId;

    @Column(name = "user_name", length = 128)
    private String userName;

    @Column(name = "password", length = 256)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "type", length = 16)
    @Builder.Default
    private UserType type = UserType.GENERAL;

    @Column(name = "status", length = 16)
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "email", length = 128)
    @Convert(converter = CryptoConverter.class)
    private String email;

    @Column(name = "mobile", length = 64)
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "user_id",
            referencedColumnName = "user_id",
            updatable = false
    )
    @Builder.Default
    private List<UserRoleEntity> userRoleEntities = new ArrayList<>();

}
