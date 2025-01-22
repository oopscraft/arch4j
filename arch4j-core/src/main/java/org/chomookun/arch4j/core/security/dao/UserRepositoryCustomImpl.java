package org.chomookun.arch4j.core.security.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.chomookun.arch4j.core.security.model.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserEntity> findAll(UserSearch userSearch, Pageable pageable) {
        QUserEntity qUserEntity = QUserEntity.userEntity;
        JPAQuery<UserEntity> query = jpaQueryFactory.select(qUserEntity)
                .from(qUserEntity);
        if(userSearch.getUsername() != null) {
            query.where(qUserEntity.username.contains(userSearch.getUsername()));
        }
        if(userSearch.isAdmin()) {
            query.where(qUserEntity.admin.eq(userSearch.isAdmin()));
        }
        if(userSearch.getStatus() != null) {
            query.where(qUserEntity.status.eq(userSearch.getStatus()));
        }

        List<UserEntity> content = query.clone()
                .orderBy(qUserEntity.systemUpdatedAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        Long total = query.clone()
                .select(qUserEntity.count())
                .fetchOne();
        total = Optional.ofNullable(total).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

}
