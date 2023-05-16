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
     */
    public void saveAuthority(Authority authority) {
        AuthorityEntity authorityEntity = authorityRepository.findById(authority.getId()).orElse(null);
        if(authorityEntity == null) {
            authorityEntity = AuthorityEntity.builder()
                    .id(authority.getId())
                    .build();
        }
        authorityEntity = authorityEntity.toBuilder()
                .name(authority.getName())
                .note(authority.getNote())
                .build();
        authorityRepository.saveAndFlush(authorityEntity);
    }

    /**
     * get authority
     * @param id id
     * @return authority
     */
    public Optional<Authority> getAuthority(String id) {
        return authorityRepository.findById(id).map(Authority::from);
    }

    /**
     * delete authority
     * @param id id
     */
    public void deleteAuthority(String id) {
        authorityRepository.deleteById(id);
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
        if(authoritySearch.getId() != null) {
           specification = specification.and(AuthoritySpecification.likeId(authoritySearch.getId()));
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
