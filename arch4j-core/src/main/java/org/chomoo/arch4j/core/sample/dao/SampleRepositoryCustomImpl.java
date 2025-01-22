package org.chomoo.arch4j.core.sample.dao;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.chomoo.arch4j.core.sample.model.SampleSearch;
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
        if(sampleSearch.getSampleName() != null) {
            query.where(qSampleEntity.sampleName.contains(sampleSearch.getSampleName()));
        }
        if(sampleSearch.getSampleType() != null) {
            query.where(qSampleEntity.sampleType.eq(sampleSearch.getSampleType()));
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
