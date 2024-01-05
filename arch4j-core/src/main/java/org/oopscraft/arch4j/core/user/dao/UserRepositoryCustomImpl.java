package org.oopscraft.arch4j.core.user.dao;

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
    public Page<UserEntity> findAll(UserSearch userSearch, Pageable pageable) {
        QUserEntity qUserEntity = QUserEntity.userEntity;
        JPAQuery<UserEntity> query = jpaQueryFactory.select(qUserEntity)
                .from(qUserEntity);

        if(userSearch.getUserId() != null) {
            query.where(qUserEntity.userId.contains(userSearch.getUserId()));
        }
        if(userSearch.getUserName() != null) {
            query.where(qUserEntity.userName.contains(userSearch.getUserName()));
        }
        if(userSearch.getUserStatus() != null) {
            query.where(qUserEntity.userStatus.eq(userSearch.getUserStatus()));
        }
        if(userSearch.isAdmin()) {
            query.where(qUserEntity.admin.eq(userSearch.isAdmin()));
        }
        if(userSearch.getEmail() != null) {
            query.where(qUserEntity.email.eq(userSearch.getEmail()));
        }
        if(userSearch.getMobile() != null) {
            query.where(qUserEntity.mobile.eq(userSearch.getMobile()));
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
