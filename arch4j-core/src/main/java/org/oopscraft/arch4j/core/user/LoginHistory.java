package org.oopscraft.arch4j.core.user;

import lombok.*;
import org.oopscraft.arch4j.core.user.dao.LoginHistoryEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHistory {

    private String userId;

    private LocalDateTime loginDateTime;

    private String ipAddress;

    private String userAgent;

    /**
     * factory method
     * @param loginHistoryEntity login history entity
     * @return login history
     */
    public static LoginHistory from(LoginHistoryEntity loginHistoryEntity) {
        return LoginHistory.builder()
                .userId(loginHistoryEntity.getUserId())
                .loginDateTime(loginHistoryEntity.getLoginDateTime())
                .ipAddress(loginHistoryEntity.getIpAddress())
                .userAgent(loginHistoryEntity.getUserAgent())
                .build();
    }

}
