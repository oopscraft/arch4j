package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.dao.UserLoginEntity;
import org.oopscraft.arch4j.core.user.dao.UserLoginRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserLoginRepository userLoginRepository;

    /**
     * saves user login history
     * @param userLogin login history
     * @return login history
     */
    public UserLogin saveUserLogin(UserLogin userLogin) {
        UserLoginEntity.Pk pk = UserLoginEntity.Pk.builder()
                .userId(userLogin.getUserId())
                .loginAt(userLogin.getLoginAt())
                .build();
        UserLoginEntity loginHistoryEntity = userLoginRepository.findById(pk).orElse(
                UserLoginEntity.builder()
                    .userId(userLogin.getUserId())
                    .loginAt(userLogin.getLoginAt())
                    .build());
        loginHistoryEntity.setIpAddress(userLogin.getIpAddress());
        loginHistoryEntity.setUserAgent(userLogin.getUserAgent());
        loginHistoryEntity = userLoginRepository.saveAndFlush(loginHistoryEntity);
        return UserLogin.from(loginHistoryEntity);
    }

    /**
     * returns user login histories
     * @param userLoginSearch search condition
     * @param pageable pagination info
     * @return list of login history
     */
    public Page<UserLogin> getUserLogins(UserLoginSearch userLoginSearch, Pageable pageable) {
        Page<UserLoginEntity> loginHistoryEntityPage = userLoginRepository.findAll(userLoginSearch, pageable);
        List<UserLogin> userLogins = loginHistoryEntityPage.stream()
                .map(UserLogin::from)
                .collect(Collectors.toList());
        return new PageImpl<>(userLogins, pageable, loginHistoryEntityPage.getTotalElements());
    }

}
