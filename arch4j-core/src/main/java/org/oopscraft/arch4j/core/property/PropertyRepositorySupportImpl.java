package org.oopscraft.arch4j.core.property;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PropertyRepositorySupportImpl implements PropertyRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Property> findProperties(PropertySearch propertySearch, Pageable pageable) {

        // query
        QProperty qProperty = QProperty.property;
        JPAQuery<Property> query = jpaQueryFactory.selectFrom(qProperty);

        Optional.ofNullable(propertySearch.getId()).ifPresent(id ->
                query.where(qProperty.id.lower().contains(id.toLowerCase())));

        Optional.ofNullable(propertySearch.getName()).ifPresent(name ->
                query.where(qProperty.name.lower().contains(name.toLowerCase())));

        // list
        List<Property> list = query.clone()
                .orderBy(qProperty.systemData.desc().nullsLast(), qProperty.id.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // count
        long count = query.clone()
                .select(qProperty.count())
                .fetchOne();

        // returns
        return new PageImpl<>(list, pageable, count);
    }
}
