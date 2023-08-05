package org.oopscraft.arch4j.core.user;

import lombok.*;
import org.oopscraft.arch4j.core.user.dao.UserLoginEntity;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserLogin {

    private String userId;

    private LocalDateTime loginAt;

    private String ipAddress;

    private String userAgent;

    public static UserLogin from(UserLoginEntity loginHistoryEntity) {
        return UserLogin.builder()
                .userId(loginHistoryEntity.getUserId())
                .loginAt(loginHistoryEntity.getLoginAt())
                .ipAddress(loginHistoryEntity.getIpAddress())
                .userAgent(loginHistoryEntity.getUserAgent())
                .build();
    }

}
