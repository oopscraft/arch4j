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
        UserEntity userEntity = userRepository.findById(user.getUserId()).orElse(
                UserEntity.builder()
                        .userId(user.getUserId())
                        .password(passwordEncoder.encode(user.getPassword()))
                        .joinAt(LocalDateTime.now())
                        .build());
        userEntity.setUserName(user.getUserName());
        userEntity.setType(user.getType());
        userEntity.setStatus(user.getStatus());
        userEntity.setEmail(user.getEmail());
        userEntity.setMobile(user.getMobile());
        userEntity.setPhoto(user.getPhoto());
        userEntity.setProfile(user.getProfile());
        userEntity.setRoles(user.getRoles().stream()
                .map(role -> RoleEntity.builder()
                            .roleId(role.getRoleId())
                            .roleName(role.getRoleName())
                            .note(role.getNote())
                            .build())
                .collect(Collectors.toList()));
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
        Page<UserEntity> userEntityPage = userRepository.findAll(userSearch, pageable);
        List<User> users = userEntityPage.getContent().stream()
                .map(User::from)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, userEntityPage.getTotalElements());
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
