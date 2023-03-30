package org.oopscraft.arch4j.core.user.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;
import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.data.crypto.CryptoConverter;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserStatus;
import org.oopscraft.arch4j.core.user.UserType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "type")
    @Builder.Default
    private UserType type = UserType.GENERAL;

    @Column(name = "status")
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;

    @Column(name = "mobile")
    private String mobile;

    @Column(name = "email")
    private String email;

    @Column(name = "photo")
    @Lob
    private String photo;

    @Column(name = "profile")
    @Lob
    private String profile;

   /**
     * roles
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            foreignKey = @ForeignKey(name = "none"),
            inverseJoinColumns = @JoinColumn(name = "role_id"),
            inverseForeignKey = @ForeignKey(name = "none")
    )
    @Builder.Default
    List<RoleEntity> roles = new ArrayList<>();

}
