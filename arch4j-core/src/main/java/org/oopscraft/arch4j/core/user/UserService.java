package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.user.dao.UserEntity;
import org.oopscraft.arch4j.core.user.dao.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * saves user
     * @param user user info
     */
    @Transactional
    public User saveUser(User user) {
        UserEntity userEntity = userRepository.findById(user.getUserId()).orElse(null);
        if(userEntity == null) {
            userEntity = UserEntity.builder()
                    .userId(user.getUserId())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .joinDateTime(LocalDateTime.now())
                    .build();
        }
        userEntity = userEntity.toBuilder()
                .name(user.getName())
                .type(user.getType())
                .status(user.getStatus())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .photo(user.getPhoto())
                .profile(user.getProfile())
                .roles(user.getRoles().stream()
                        .map(role -> RoleEntity.builder()
                                    .roleId(role.getRoleId())
                                    .name(role.getName())
                                    .note(role.getNote())
                                    .build())
                        .collect(Collectors.toList()))
                .build();
        userEntity = userRepository.saveAndFlush(userEntity);
        return User.from(userEntity);
    }

    /**
     * get user
     * @param id id
     * @return user
     */
    public Optional<User> getUser(String id) {
        return userRepository.findById(id).map(User::from);
    }

    /**
     * get user by email
     * @param email email address
     * @return user
     */
    public Optional<User> getUserByEmail(String email) {
        User user = Optional.ofNullable(userRepository.findFirstByEmail(email))
                .map(User::from)
                .orElse(null);
        return Optional.ofNullable(user);
    }

    /**
     * delete user
     * @param id user id
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
        userRepository.flush();
    }

    /**
     * find users
     * @param userSearch search condition
     * @param pageable pageable
     * @return users
     */
    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        Page<UserEntity> userEntityPage = userRepository.findUsers(userSearch, pageable);
        List<User> users = userEntityPage.getContent().stream()
                .map(User::from)
                .collect(Collectors.toList());
        long total = userEntityPage.getTotalElements();
        return new PageImpl<>(users, pageable, total);
    }

    /**
     * update password
     * @param userId user id
     * @param password password
     */
    public void changePassword(String userId, String password) {
        userRepository.findById(userId).ifPresent(userEntity -> {
            userEntity.setPassword(passwordEncoder.encode(password));
            userRepository.saveAndFlush(userEntity);
        });
    }

}
