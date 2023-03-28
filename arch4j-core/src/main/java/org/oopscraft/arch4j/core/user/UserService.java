package org.oopscraft.arch4j.core.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(User user) {
        User one = userRepository.findById(user.getId()).orElse(null);
        if(one == null) {
            one = User.builder()
                    .id(user.getId())
                    .build();
        }
        one.setName(user.getName());
        one.setNickname(user.getNickname());
        one.setType(user.getType());
        one.setStatus(user.getStatus());
        userRepository.saveAndFlush(one);
    }

    /**
     * find users
     * @param userSearch search condition
     * @param pageable pageable
     * @return users
     */
    public Page<User> getUsers(UserSearch userSearch, Pageable pageable) {
        return userRepository.findUsers(userSearch, pageable);
    }

    /**
     * get user
     * @param id id
     * @return user
     */
    public Optional<User> getUser(String id) {
        return userRepository.findById(id);
    }

    /**
     * delete user
     * @param id user id
     */
    public void deleteUser(String id) {
        userRepository.deleteById(id);
        userRepository.flush();
    }

}
