package org.oopscraft.arch4j.core.code.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.code.CodeSearch;
import org.oopscraft.arch4j.core.code.entity.CodeEntity;
import org.oopscraft.arch4j.core.code.entity.QCodeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class CodeRepositorySupportImpl implements CodeRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<CodeEntity> findCodes(CodeSearch codeSearch, Pageable pageable) {

        // query
        QCodeEntity qCodeEntity = QCodeEntity.codeEntity;
        JPAQuery<CodeEntity> query = jpaQueryFactory.select(qCodeEntity)
                .from(qCodeEntity);
        Optional.ofNullable(codeSearch.getId()).ifPresent(id ->
                query.where(qCodeEntity.id.contains(id)));
        Optional.ofNullable(codeSearch.getName()).ifPresent(name ->
                query.where(qCodeEntity.name.contains(name)));

        // content
        List<CodeEntity> content = query.clone()
                .orderBy(qCodeEntity.systemUpdateDateTime.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // total count
        Long total = query.clone()
                .select(qCodeEntity.count())
                .fetchOne();
        total = Optional.ofNullable(total).orElse(0L);

        // return page
        return new PageImpl<>(content, pageable, total);
    }
}
