package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.dao.LoginHistoryEntity;
import org.oopscraft.arch4j.core.user.dao.LoginHistoryRepository;
import org.oopscraft.arch4j.core.user.dao.LoginHistorySpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LoginHistoryService {

    private final LoginHistoryRepository loginHistoryRepository;

    /**
     * saves login history
     * @param loginHistory login history
     * @return login history
     */
    public LoginHistory saveLoginHistory(LoginHistory loginHistory) {
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
        loginHistoryEntity.setIpAddress(loginHistory.getIpAddress());
        loginHistoryEntity.setUserAgent(loginHistory.getUserAgent());
        loginHistoryEntity = loginHistoryRepository.saveAndFlush(loginHistoryEntity);
        return LoginHistory.from(loginHistoryEntity);
    }

    /**
     * returns login histories
     * @param loginHistorySearch search condition
     * @param pageable pagination info
     * @return list of login history
     */
    public Page<LoginHistory> getLoginHistories(LoginHistorySearch loginHistorySearch, Pageable pageable) {
        Specification<LoginHistoryEntity> specification = (root, query, criteriaBuilder) -> null;
        if(loginHistorySearch.getUserId() != null) {
            specification = specification.and(LoginHistorySpecification.likeUserId(loginHistorySearch.getUserId()));
        }
        if(loginHistorySearch.getIpAddress() != null) {
            specification = specification.and(LoginHistorySpecification.likeIpAddress(loginHistorySearch.getIpAddress()));
        }
        Page<LoginHistoryEntity> loginHistoryEntityPage = loginHistoryRepository.findAll(specification, pageable);
        List<LoginHistory> loginHistories = loginHistoryEntityPage.stream()
                .map(LoginHistory::from)
                .collect(Collectors.toList());
        long total = loginHistoryEntityPage.getTotalElements();
        return new PageImpl<>(loginHistories, pageable, total);
    }

}
