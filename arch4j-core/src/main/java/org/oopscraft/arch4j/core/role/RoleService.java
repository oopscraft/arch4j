package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.repository.AuthorityEntity;
import org.oopscraft.arch4j.core.role.repository.RoleEntity;
import org.oopscraft.arch4j.core.role.repository.RoleRepository;
import org.oopscraft.arch4j.core.role.repository.RoleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    /**
     * save role
     * @param role role
     * @return role
     */
    public Role saveRole(Role role) {
        RoleEntity roleEntity = roleRepository.findById(role.getId()).orElse(null);
        if(roleEntity == null) {
            roleEntity = RoleEntity.builder()
                    .id(role.getId())
                    .build();
        }
        roleEntity = roleEntity.toBuilder()
                .name(role.getName())
                .note(role.getNote())
                .authorities(role.getAuthorities().stream()
                        .map(authority -> {
                            return AuthorityEntity.builder()
                                    .id(authority.getId())
                                    .name(authority.getName())
                                    .note(authority.getNote())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .build();
        roleEntity = roleRepository.saveAndFlush(roleEntity);
        return Role.from(roleEntity);
    }

    /**
     * get role
     * @param id id
     * @return role
     */
    public Optional<Role> getRole(String id) {
        return roleRepository.findById(id).map(Role::from);
    }

    /**
     * delete role
     * @param id role id
     */
    public void deleteRole(String id) {
        roleRepository.deleteById(id);
        roleRepository.flush();
    }

    /**
     * find role
     * @param roleSearch search condition
     * @param pageable pageable
     * @return role page
     */
    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {

        // search condition
        Specification<RoleEntity> specification = (root, query, criteriaBuilder) -> null;
        if(roleSearch.getId() != null) {
           specification = specification.and(RoleSpecification.likeId(roleSearch.getId()));
        }
        if(roleSearch.getName() != null) {
            specification = specification.and(RoleSpecification.likeName(roleSearch.getName()));
        }

        // find data
        Page<RoleEntity> roleEntityPage = roleRepository.findAll(specification, pageable);
        List<Role> roles = roleEntityPage.getContent().stream()
                .map(Role::from)
                .collect(Collectors.toList());
        long total = roleEntityPage.getTotalElements();

        // return
        return new PageImpl<>(roles, pageable, total);
    }

}
