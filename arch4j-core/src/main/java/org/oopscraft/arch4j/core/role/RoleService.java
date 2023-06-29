package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.dao.AuthorityEntity;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.role.dao.RoleRepository;
import org.oopscraft.arch4j.core.role.dao.RoleSpecification;
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
        RoleEntity roleEntity = roleRepository.findById(role.getRoleId()).orElse(null);
        if(roleEntity == null) {
            roleEntity = RoleEntity.builder()
                    .roleId(role.getRoleId())
                    .build();
        }
        roleEntity = roleEntity.toBuilder()
                .name(role.getName())
                .note(role.getNote())
                .authorities(role.getAuthorities().stream()
                        .map(authority -> AuthorityEntity.builder()
                                    .authorityId(authority.getAuthorityId())
                                    .name(authority.getName())
                                    .note(authority.getNote())
                                    .build())
                        .collect(Collectors.toList()))
                .build();
        roleEntity = roleRepository.saveAndFlush(roleEntity);
        return Role.from(roleEntity);
    }

    /**
     * get role
     * @param roleId role id
     * @return role
     */
    public Optional<Role> getRole(String roleId) {
        return roleRepository.findById(roleId).map(Role::from);
    }

    /**
     * delete role
     * @param roleId role id
     */
    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
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
        if(roleSearch.getRoleId() != null) {
           specification = specification.and(RoleSpecification.likeRoleId(roleSearch.getRoleId()));
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
