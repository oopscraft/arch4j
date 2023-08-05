package org.oopscraft.arch4j.core.user.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "core_user_login",
        indexes = {
                @Index(name = "ix_ip_address", columnList = "ip_address")
        })
@IdClass(UserLoginEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLoginEntity extends SystemFieldEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Pk implements Serializable {
        private String userId;
        private LocalDateTime loginAt;
    }

    @Id
    @Column(name = "user_id", length = 32)
    private String userId;

    @Id
    @Column(name = "login_at")
    private LocalDateTime loginAt;

    @Column(name = "ip_address", length = 32)
    private String ipAddress;

    @Column(name = "user_agent", length = 128)
    private String userAgent;

}
