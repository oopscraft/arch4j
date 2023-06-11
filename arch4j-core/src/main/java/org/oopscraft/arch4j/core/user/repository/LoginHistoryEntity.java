package org.oopscraft.arch4j.core.user.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "core_login_history",
    indexes = {
        @Index(columnList = "login_datetime")
    }
)
@IdClass(LoginHistoryEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryEntity extends SystemFieldEntity {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Pk implements Serializable {
        private String userId;
        private LocalDateTime loginDateTime;
    }

    @Id
    @Column(name = "user_id", length = 64)
    private String userId;

    @Id
    @Column(name = "login_datetime")
    private LocalDateTime loginDateTime;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "user_agent")
    private String userAgent;

}
