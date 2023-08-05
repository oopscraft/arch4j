package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.Role;
import org.oopscraft.arch4j.core.role.RoleService;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.user.dao.UserEntity;
import org.oopscraft.arch4j.core.user.dao.UserRepository;
import org.oopscraft.arch4j.core.user.dao.UserRoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EntityManager entityManager;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

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

        userEntity.getUserRoleEntities().clear();
        user.getRoles().forEach(role -> {
            UserRoleEntity userRoleEntity = UserRoleEntity.builder()
                    .userId(userEntity.getUserId())
                    .roleId(role.getRoleId())
                    .build();
            userEntity.getUserRoleEntities().add(userRoleEntity);
        });

        userRepository.saveAndFlush(userEntity);
        return getUser(userEntity.getUserId())
                .orElseThrow();
    }

    public Optional<User> getUser(String userId) {
        return userRepository.findById(userId)
                .map(this::mapToUser);
    }

    private User mapToUser(UserEntity userEntity) {
        User user = User.builder()
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .password(userEntity.getPassword())
                .type(userEntity.getType())
                .status(userEntity.getStatus())
                .email(userEntity.getEmail())
                .mobile(userEntity.getMobile())
                .joinAt(userEntity.getJoinAt())
                .loginAt(userEntity.getCloseAt())
                .photo(userEntity.getPhoto())
                .profile(userEntity.getProfile())
                .build();
        List<Role> roles = userEntity.getUserRoleEntities().stream()
                .map(userRoleEntity -> {
                    RoleEntity roleEntity = userRoleEntity.getRoleEntity();
                    return Optional.ofNullable(roleEntity)
                            .map(roleService::mapToRole)
                            .orElse(null);
                })
                .collect(Collectors.toList());
        user.setRoles(roles);
        return user;
    }

    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        Page<UserEntity> userEntityPage = userRepository.findAll(userSearch, pageable);
        List<User> users = userEntityPage.getContent().stream()
                .map(this::mapToUser)
                .collect(Collectors.toList());
        return new PageImpl<>(users, pageable, userEntityPage.getTotalElements());
    }

    public Optional<User> getUserByEmail(String email) {
        User user = Optional.ofNullable(userRepository.findFirstByEmail(email))
                .map(this::mapToUser)
                .orElse(null);
        return Optional.ofNullable(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
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
