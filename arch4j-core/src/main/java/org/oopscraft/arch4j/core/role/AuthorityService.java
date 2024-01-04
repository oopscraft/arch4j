package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.dao.AuthorityEntity;
import org.oopscraft.arch4j.core.role.dao.AuthorityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public Authority saveAuthority(Authority authority) {
        AuthorityEntity authorityEntity = authorityRepository.findById(authority.getAuthorityId()).orElse(
                AuthorityEntity.builder()
                    .authorityId(authority.getAuthorityId())
                    .build());
        authorityEntity.setSystemUpdatedAt(LocalDateTime.now());
        authorityEntity.setAuthorityName(authority.getAuthorityName());
        authorityEntity.setNote(authority.getNote());
        AuthorityEntity savedAuthorityEntity = authorityRepository.saveAndFlush(authorityEntity);
        return Authority.from(savedAuthorityEntity);
    }

    public Optional<Authority> getAuthority(String authorityId) {
        return authorityRepository.findById(authorityId)
                .map(Authority::from);
    }

    public void deleteAuthority(String authorityId) {
        authorityRepository.deleteById(authorityId);
        authorityRepository.flush();
    }

    public List<Authority> getAuthorities() {
        return authorityRepository.findAll().stream()
                .map(Authority::from)
                .collect(Collectors.toList());
    }

    public Page<Authority> getAuthorities(AuthoritySearch authoritySearch, Pageable pageable) {
        Page<AuthorityEntity> authorityEntityPage = authorityRepository.findAll(authoritySearch, pageable);
        List<Authority> authorities = authorityEntityPage.getContent().stream()
                .map(Authority::from)
                .collect(Collectors.toList());
        return new PageImpl<>(authorities, pageable, authorityEntityPage.getTotalElements());
    }

}
