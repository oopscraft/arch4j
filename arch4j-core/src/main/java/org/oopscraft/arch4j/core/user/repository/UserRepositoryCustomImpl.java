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
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<UserEntity> findUsers(UserSearch userSearch, Pageable pageable) {

        // query
        QUserEntity qUserEntity = QUserEntity.userEntity;
        JPAQuery<UserEntity> query = jpaQueryFactory.select(qUserEntity)
                .from(qUserEntity);
        if(userSearch.getUserId() != null) {
            query.where(qUserEntity.userId.contains(userSearch.getUserId()));
        }
        if(userSearch.getName() != null) {
            query.where(qUserEntity.name.contains(userSearch.getName()));
        }
        if(userSearch.getType() != null) {
            query.where(qUserEntity.type.eq(userSearch.getType()));
        }
        if(userSearch.getStatus() != null) {
            query.where(qUserEntity.status.eq(userSearch.getStatus()));
        }
        if(userSearch.getEmail() != null) {
            query.where(qUserEntity.email.contains(userSearch.getName()));
        }
        if(userSearch.getMobile() != null) {
            query.where(qUserEntity.mobile.contains(userSearch.getMobile()));
        }

        // content
        List<UserEntity> content = query.clone()
                .orderBy(qUserEntity.systemUpdatedAt.desc())
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
