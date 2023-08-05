package org.oopscraft.arch4j.core.user.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.oopscraft.arch4j.core.test.CoreTestSupport;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserLoginRepositoryTest extends CoreTestSupport {

    private final UserLoginRepository userLoginRepository;

    UserLoginEntity getTestData() {
        return UserLoginEntity.builder()
                .userId("test-user")
                .loginAt(LocalDateTime.now())
                .ipAddress("127.0.0.1")
                .userAgent("UserAgent:!!!!")
                .build();
    }

    @Test
    @Order(1)
    void saveToPersist() {
        // given
        UserLoginEntity userLoginEntity = getTestData();

        // when
        userLoginRepository.saveAndFlush(userLoginEntity);
        entityManager.clear();

        // then
        assertNotNull(entityManager.find(UserLoginEntity.class, UserLoginEntity.Pk.builder()
                .userId(userLoginEntity.getUserId())
                .loginAt(userLoginEntity.getLoginAt())
                .build()));
    }

}