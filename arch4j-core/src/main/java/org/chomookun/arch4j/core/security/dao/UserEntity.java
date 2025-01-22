package org.chomookun.arch4j.core.security.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomookun.arch4j.core.common.data.BaseEntity;
import org.chomookun.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomookun.arch4j.core.common.data.converter.BooleanConverter;
import org.chomookun.arch4j.core.common.data.converter.CryptoConverter;
import org.chomookun.arch4j.core.security.model.User;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
    name = "core_user",
    indexes = {
        @Index(name = "ix_username", columnList = "username"),
        @Index(name = "ix_name", columnList = "name"),
        @Index(name = "ix_email", columnList = "email"),
        @Index(name = "ix_mobile", columnList = "mobile"),
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

    @Column(name = "email", unique = true, length = 128)
    @Convert(converter = CryptoConverter.class)
    private String email;

    @Column(name = "mobile", unique = true, length = 64)
    @Convert(converter = CryptoConverter.class)
    private String mobile;

    @Column(name = "photo")
    @Lob
    private String photo;

    @Column(name = "profile")
    @Lob
    private String profile;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id", updatable = false)
    @Builder.Default
    private List<UserRoleEntity> userRoles = new ArrayList<>();

    @Converter(autoApply = true)
    public static class StatusConverter extends AbstractEnumConverter<User.Status> {}

}
