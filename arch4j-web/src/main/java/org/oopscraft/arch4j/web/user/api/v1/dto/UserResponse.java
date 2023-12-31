package org.oopscraft.arch4j.web.user.api.v1.dto;

import lombok.*;
import org.oopscraft.arch4j.core.user.User;
import org.oopscraft.arch4j.core.user.UserStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    private String userId;

    private String userName;

    private boolean admin;

    private UserStatus status;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .userName(user.getUserName())
                .admin(user.isAdmin())
                .status(user.getStatus())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .photo(user.getPhoto())
                .profile(user.getProfile())
                .build();
    }

}
