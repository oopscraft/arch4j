package org.oopscraft.arch4j.core.security.service;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.common.data.IdGenerator;
import org.oopscraft.arch4j.core.security.model.Role;
import org.oopscraft.arch4j.core.security.dao.UserEntity;
import org.oopscraft.arch4j.core.security.dao.UserRepository;
import org.oopscraft.arch4j.core.security.dao.UserRoleEntity;
import org.oopscraft.arch4j.core.security.model.User;
import org.oopscraft.arch4j.core.security.model.UserSearch;
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

    @Transactional
    public User saveUser(User user) {
        UserEntity userEntity;
        if (user.getUserId() != null) {
            userEntity = userRepository.findById(user.getUserId()).orElseThrow();
        } else {
            userEntity = UserEntity.builder()
                    .userId(IdGenerator.uuid())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .build();
        }
        userEntity.setSystemUpdatedAt(LocalDateTime.now()); // disable dirty checking
        userEntity.setUsername(user.getUsername());
        userEntity.setName(user.getName());
        userEntity.setAdmin(user.isAdmin());
        userEntity.setStatus(user.getStatus());
        userEntity.setEmail(user.getEmail());
        userEntity.setMobile(user.getMobile());
        userEntity.setPhoto(user.getPhoto());
        userEntity.setProfile(user.getProfile());

        // user roles
        userEntity.getUserRoles().clear();
        for(Role role : user.getRoles()) {
            UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                    .userId(userEntity.getUserId())
                    .roleId(role.getRoleId())
                    .build();
            userEntity.getUserRoles().add(userRoleEntity);
        }

        // save
        UserEntity savedUserEntity = userRepository.saveAndFlush(userEntity);

        // return
        return User.from(savedUserEntity);
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId)
                .map(User::from);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::from);
    }

    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        Page<UserEntity> userEntityPage = userRepository.findAll(userSearch, pageable);
        List<User> users = userEntityPage.getContent().stream()
                .map(User::from)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, userEntityPage.getTotalElements());
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
        userRepository.flush();
    }

    public boolean isPasswordMatched(String userId, String password) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow();
        return passwordEncoder.matches(password, userEntity.getPassword());
    }

    public void changePassword(String userId, String newPassword) {
        userRepository.findById(userId).ifPresent(userEntity -> {
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.saveAndFlush(userEntity);
        });
    }

}
