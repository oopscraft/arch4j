package org.oopscraft.arch4j.core.user.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.user.UserSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositorySupportImpl implements UserRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserEntity> findUsers(UserSearch userSearch, Pageable pageable) {

        // query
        QUserEntity qUserEntity = QUserEntity.userEntity;
        JPAQuery<UserEntity> query = jpaQueryFactory.select(qUserEntity)
                .from(qUserEntity);
        Optional.ofNullable(userSearch.getId()).ifPresent(id -> {
            query.where(qUserEntity.id.contains(id));
        });
        Optional.ofNullable(userSearch.getName()).ifPresent(name -> {
            query.where(qUserEntity.name.contains(name));
        });
        Optional.ofNullable(userSearch.getEmail()).ifPresent(email -> {
            query.where(qUserEntity.email.contains(email));
        });
        Optional.ofNullable(userSearch.getMobile()).ifPresent(mobile -> {
            query.where(qUserEntity.mobile.contains(mobile));
        });
        Optional.ofNullable(userSearch.getType()).ifPresent(type -> {
            query.where(qUserEntity.type.eq(type));
        });
        Optional.ofNullable(userSearch.getStatus()).ifPresent(status -> {
            query.where(qUserEntity.status.eq(status));
        });

        // content
        List<UserEntity> content = query.clone()
                .orderBy(qUserEntity.systemUpdateDateTime.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // total count
        Long total = query.clone()
                .select(qUserEntity.count())
                .fetchOne();
        total = Optional.ofNullable(total).orElse(0L);

        // returns page
        return new PageImpl<>(content, pageable, total);
    }

}
