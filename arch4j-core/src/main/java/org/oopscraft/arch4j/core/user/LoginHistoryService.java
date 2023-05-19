package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.repository.LoginHistoryEntity;
import org.oopscraft.arch4j.core.user.repository.LoginHistoryRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    /**
     * saves login history
     * @param loginHistory login history
     */
    public void saveLoginHistory(LoginHistory loginHistory) {
        LoginHistoryEntity.Pk pk = LoginHistoryEntity.Pk.builder()
                .userId(loginHistory.getUserId())
                .loginDateTime(loginHistory.getLoginDateTime())
                .build();
        LoginHistoryEntity loginHistoryEntity = loginHistoryRepository.findById(pk).orElse(null);
        if(loginHistoryEntity == null) {
            loginHistoryEntity = LoginHistoryEntity.builder()
                    .userId(loginHistory.getUserId())
                    .loginDateTime(loginHistory.getLoginDateTime())
                    .build();
        }
        loginHistoryEntity.setLoginSuccess(loginHistory.getLoginSuccess());
        loginHistoryEntity.setIpAddress(loginHistory.getIpAddress());
        loginHistoryEntity.setUserAgent(loginHistory.getUserAgent());
        loginHistoryRepository.saveAndFlush(loginHistoryEntity);
    }


}
