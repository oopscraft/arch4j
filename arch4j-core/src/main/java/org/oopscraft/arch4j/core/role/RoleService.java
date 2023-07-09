package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.dao.AuthorityEntity;
import org.oopscraft.arch4j.core.role.dao.RoleEntity;
import org.oopscraft.arch4j.core.role.dao.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role saveRole(Role role) {
        RoleEntity roleEntity = roleRepository.findById(role.getRoleId()).orElse(
            roleEntity = RoleEntity.builder()
                    .roleId(role.getRoleId())
                    .build());
        roleEntity.setRoleName(role.getRoleName());
        roleEntity.setNote(role.getNote());
        roleEntity.setAuthorities(role.getAuthorities().stream()
                        .map(authority -> AuthorityEntity.builder()
                                    .authorityId(authority.getAuthorityId())
                                    .authorityName(authority.getAuthorityName())
                                    .note(authority.getNote())
                                    .build())
                        .collect(Collectors.toList()));
        roleEntity = roleRepository.saveAndFlush(roleEntity);
        return Role.from(roleEntity);
    }

    public Optional<Role> getRole(String roleId) {
        return roleRepository.findById(roleId).map(Role::from);
    }

    public void deleteRole(String roleId) {
        roleRepository.deleteById(roleId);
        roleRepository.flush();
    }

    public Page<Role> getRoles(RoleSearch roleSearch, Pageable pageable) {
        Page<RoleEntity> roleEntityPage = roleRepository.findAll(roleSearch, pageable);
        List<Role> roles = roleEntityPage.getContent().stream()
                .map(Role::from)
                .collect(Collectors.toList());
        return new PageImpl<>(roles, pageable, roleEntityPage.getTotalElements());
    }

}
