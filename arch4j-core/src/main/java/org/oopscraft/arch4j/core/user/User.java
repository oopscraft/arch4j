package org.oopscraft.arch4j.core.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldSupport;
import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;

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
public class User extends SystemFieldSupport {

    /**
     * user type
     */
    public enum Type {
        GENERAL, SYSTEM
    }

    static class TypeConverter extends AbstractEnumConverter<Type> {}

    /**
     * user status
     */
    public enum Status {
        ACTIVE, LOCKED, EXPIRED
    }

    static class StatusConverter extends AbstractEnumConverter<Status> {}

    @Id
    @Column(name = "id", length = 64)
    private String id;

    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "type")
    @Convert(converter = TypeConverter.class)
    @Builder.Default
    private Type type = Type.GENERAL;

    @Column(name = "status")
    @Convert(converter = StatusConverter.class)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "name")
    private String name;

    @Column(name = "nickname")
    private String nickname;

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
    List<Role> roles = new ArrayList<>();

}
