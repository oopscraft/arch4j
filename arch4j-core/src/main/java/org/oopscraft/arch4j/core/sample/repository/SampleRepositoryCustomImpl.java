package org.oopscraft.arch4j.core.sample.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.sample.SampleSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SampleRepositoryCustomImpl implements SampleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<SampleEntity> findSamples(SampleSearch sampleSearch, Pageable pageable) {

        // query
        QSampleEntity qSampleEntity = QSampleEntity.sampleEntity;
        JPAQuery<SampleEntity> query = jpaQueryFactory
                .selectFrom(qSampleEntity);

        // where condition
        if(sampleSearch.getSampleId() != null) {
            query.where(qSampleEntity.sampleId.contains(sampleSearch.getSampleId()));
        }
        if(sampleSearch.getName() != null) {
            query.where(qSampleEntity.name.contains(sampleSearch.getName()));
        }
        if(sampleSearch.getType() != null) {
            query.where(qSampleEntity.type.eq(sampleSearch.getType()));
        }

        // content
        List<SampleEntity> content = query.clone()
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        // total
        Long total = query.clone()
                .select(qSampleEntity.count())
                .fetchOne();
        total = Optional.ofNullable(total).orElse(0L);

        // return
        return new PageImpl<>(content, pageable, total);
    }


}
