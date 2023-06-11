package org.oopscraft.arch4j.core.role;

import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.role.repository.AuthorityEntity;
import org.oopscraft.arch4j.core.role.repository.AuthorityRepository;
import org.oopscraft.arch4j.core.role.repository.AuthoritySpecification;
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
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    /**
     * save authority
     * @param authority authority
     * @return authority
     */
    public Authority saveAuthority(Authority authority) {
        AuthorityEntity authorityEntity = authorityRepository.findById(authority.getAuthorityId()).orElse(null);
        if(authorityEntity == null) {
            authorityEntity = AuthorityEntity.builder()
                    .authorityId(authority.getAuthorityId())
                    .build();
        }
        authorityEntity = authorityEntity.toBuilder()
                .name(authority.getName())
                .note(authority.getNote())
                .build();
        authorityEntity = authorityRepository.saveAndFlush(authorityEntity);
        return Authority.from(authorityEntity);
    }

    /**
     * get authority
     * @param authorityId authority id
     * @return authority
     */
    public Optional<Authority> getAuthority(String authorityId) {
        return authorityRepository.findById(authorityId).map(Authority::from);
    }

    /**
     * delete authority
     * @param authorityId id
     */
    public void deleteAuthority(String authorityId) {
        authorityRepository.deleteById(authorityId);
        authorityRepository.flush();
    }

    /**
     * find authorities
     * @param authoritySearch search condition
     * @param pageable pageable
     * @return authority page
     */
    public Page<Authority> getAuthorities(AuthoritySearch authoritySearch, Pageable pageable) {

        // search condition
        Specification<AuthorityEntity> specification = (root, query, criteriaBuilder) -> null;
        if(authoritySearch.getAuthorityId() != null) {
           specification = specification.and(AuthoritySpecification.likeAuthorityId(authoritySearch.getAuthorityId()));
        }
        if(authoritySearch.getName() != null) {
            specification = specification.and(AuthoritySpecification.likeName(authoritySearch.getName()));
        }

        // find data
        Page<AuthorityEntity> authorityEntityPage = authorityRepository.findAll(specification, pageable);
        List<Authority> authorities = authorityEntityPage.getContent().stream()
                .map(Authority::from)
                .collect(Collectors.toList());
        long total = authorityEntityPage.getTotalElements();

        // return
        return new PageImpl<>(authorities, pageable, total);
    }

}
