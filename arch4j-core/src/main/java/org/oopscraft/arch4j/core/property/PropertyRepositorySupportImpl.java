package org.oopscraft.arch4j.core.property;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PropertyRepositorySupportImpl implements PropertyRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Property> findProperties(String id, String name, Pageable pageable) {

        // query
        QProperty qProperty = QProperty.property;
        JPAQuery<Property> query = jpaQueryFactory.selectFrom(qProperty);
        query.where(
                Optional.ofNullable(id).map(qProperty.id::contains).orElse(null),
                Optional.ofNullable(name).map(qProperty.name::contains).orElse(null)
        );

        // content
        List<Property> content = query.clone()
                .orderBy(qProperty.systemRequired.desc().nullsLast(), qProperty.id.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // total
        long total = query.clone()
                .select(qProperty.count())
                .fetchOne();

        // returns
        return new PageImpl<>(content, pageable, total);
    }

}
