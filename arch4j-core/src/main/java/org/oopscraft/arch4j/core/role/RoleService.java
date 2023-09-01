package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.dao.RoleAuthorityEntity;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.role.dao.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    @Transactional
    public Role saveRole(Role role) {
        final RoleEntity roleEntity = roleRepository.findById(role.getRoleId())
                .orElse(RoleEntity.builder()
                    .roleId(role.getRoleId())
                    .build());
        roleEntity.setRoleName(role.getRoleName());
        roleEntity.setNote(role.getNote());

        // authorities
        roleEntity.getRoleAuthorityEntities().clear();
        role.getAuthorities().forEach(authority -> {
            RoleAuthorityEntity roleAuthorityEntity = RoleAuthorityEntity.builder()
                    .roleId(roleEntity.getRoleId())
                    .authorityId(authority.getAuthorityId())
                    .build();
            roleEntity.getRoleAuthorityEntities().add(roleAuthorityEntity);
        });

        // save
        RoleEntity savedRoleEntity = roleRepository.saveAndFlush(roleEntity);

        // return
        return Role.from(savedRoleEntity);
    }

    public Optional<Role> getRole(String roleId) {
        return roleRepository.findById(roleId)
                .map(Role::from);
    }

    public List<Role> getRoles() {
        return roleRepository.findAll().stream()
                .map(Role::from)
                .collect(Collectors.toList());
    }

    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        Page<RoleEntity> roleEntityPage = roleRepository.findAll(roleSearch, pageable);
        List<Role> roles = roleEntityPage.getContent().stream()
                .map(Role::from)
                .collect(Collectors.toList());
        return new PageImpl<>(roles, pageable, roleEntityPage.getTotalElements());
    }

    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
        roleRepository.flush();
    }

}
