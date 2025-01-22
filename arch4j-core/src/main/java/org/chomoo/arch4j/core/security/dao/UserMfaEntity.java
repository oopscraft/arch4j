package org.chomoo.arch4j.core.security.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseEntity;
import org.chomoo.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomoo.arch4j.core.security.model.UserMfa;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_mfa")
@IdClass(UserMfaEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMfaEntity extends BaseEntity {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String userId;
        private UserMfa.Type type;
    }

    @Id
    @Column(name = "user_id", length = 32)
    private String userId;

    @Id
    @Column(name = "type", length = 16)
    private UserMfa.Type type;

    @Column(name = "enabled", length = 1)
    private boolean mfaEnabled;

    @Column(name = "secret", length = 255)
    private String secret;

    @Converter(autoApply = true)
    public static class TypeConverter extends AbstractEnumConverter<UserMfa.Type> {}


}
