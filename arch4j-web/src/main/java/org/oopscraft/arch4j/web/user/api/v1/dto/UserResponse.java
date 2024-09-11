package org.oopscraft.arch4j.web.user.api.v1.dto;

import lombok.*;
import org.oopscraft.arch4j.core.security.model.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponse {

    private String userId;

    private String username;

    private String email;

    private String mobile;

    private String photo;

    private String profile;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .mobile(user.getMobile())
                .photo(user.getPhoto())
                .profile(user.getProfile())
                .build();
    }

}
