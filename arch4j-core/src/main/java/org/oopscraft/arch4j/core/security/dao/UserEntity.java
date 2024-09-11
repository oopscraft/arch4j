package org.oopscraft.arch4j.core.security.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.BaseEntity;
import org.oopscraft.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.common.data.converter.BooleanConverter;
import org.oopscraft.arch4j.core.common.data.converter.CryptoConverter;
import org.oopscraft.arch4j.core.security.model.User;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "core_user",
    indexes = {
        @Index(name = "ix_username", columnList = "username"),
        @Index(name = "ix_name", columnList = "name"),
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

    @Column(name = "username", unique = true, length = 128)
    private String username;

    @Column(name = "password", length = 256)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "name", length = 128)
    private String name;

    @Column(name = "admin", length = 1)
    @Convert(converter = BooleanConverter.class)
    private boolean admin;

    @Column(name = "status", length = 16)
    private User.Status status;

    @Column(name = "mobile", unique = true, length = 64)
    @Convert(converter = CryptoConverter.class)
    private String mobile;

    @Column(name = "photo")
    @Lob
    private String photo;

    @Column(name = "profile")
    @Lob
    private String profile;

    @Column(name = "join_at")
    private Instant joinAt;

    @Column(name = "close_at")
    private Instant closeAt;

    @Column(name = "password_at")
    private Instant passwordAt;

    @Column(name = "expire_at")
    private Instant expireAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", updatable = false)
    @Builder.Default
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    @Converter(autoApply = true)
    public static class StatusConverter extends AbstractEnumConverter<User.Status> {}

}
