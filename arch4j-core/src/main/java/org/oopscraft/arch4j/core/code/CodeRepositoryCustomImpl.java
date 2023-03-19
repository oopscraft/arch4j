package org.oopscraft.arch4j.core.code;

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
public class CodeRepositoryCustomImpl implements CodeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Code> findCodes(CodeSearch codeSearch, Pageable pageable) {

        // query
        QCode qCode = QCode.code;
        JPAQuery<Code> query = jpaQueryFactory.select(qCode)
                .from(qCode);
        Optional.ofNullable(codeSearch.getId()).ifPresent(id ->
                query.where(qCode.id.contains(id)));
        Optional.ofNullable(codeSearch.getName()).ifPresent(name ->
                query.where(qCode.name.contains(name)));

        // list
        List<Code> content = query.clone()
                .orderBy(qCode.systemUpdateDateTime.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
        // count
        long total = query.clone()
                .select(qCode.count())
                .fetchOne();

        // returns
        return new PageImpl<>(content, pageable, total);

    }
}
