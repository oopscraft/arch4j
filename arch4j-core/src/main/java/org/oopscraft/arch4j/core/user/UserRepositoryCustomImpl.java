package org.oopscraft.arch4j.core.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<User> findUsers(UserSearch userSearch, Pageable pageable) {

        // query
        QUser qUser = QUser.user;
        JPAQuery<User> query = jpaQueryFactory.select(qUser)
                .from(qUser);
        Optional.ofNullable(userSearch.getId()).ifPresent(id -> {
            query.where(qUser.id.contains(id));
        });
        Optional.ofNullable(userSearch.getName()).ifPresent(name -> {
            query.where(qUser.name.contains(name));
        });

        // content
        List<User> content = query.clone()
                .orderBy(qUser.systemUpdateDateTime.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // total count
        Long total = query.clone()
                .select(qUser.count())
                .fetchOne();
        total = Optional.ofNullable(total).orElse(0L);

        // returns page
        return new PageImpl<>(content, pageable, total);
    }

}
