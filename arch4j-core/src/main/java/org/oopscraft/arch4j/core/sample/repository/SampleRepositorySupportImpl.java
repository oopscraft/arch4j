package org.oopscraft.arch4j.core.sample.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.oopscraft.arch4j.core.sample.SampleType;
import org.oopscraft.arch4j.core.sample.entity.QSampleEntity;
import org.oopscraft.arch4j.core.sample.vo.SampleVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SampleRepositorySupportImpl implements SampleRepositorySupport {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<SampleVo> findSamples(String id, String name, SampleType type, Pageable pageable) {

        // query
        QSampleEntity qSampleEntity = QSampleEntity.sampleEntity;
        JPAQuery<SampleVo> query = jpaQueryFactory
                .select(Projections.fields(SampleVo.class,
                        qSampleEntity.id,
                        qSampleEntity.name,
                        qSampleEntity.type.stringValue().as("type"),
                        qSampleEntity.number,
                        qSampleEntity.longNumber,
                        qSampleEntity.doubleNumber,
                        qSampleEntity.bigDecimal,
                        qSampleEntity.sqlDate,
                        qSampleEntity.utilDate,
                        qSampleEntity.timestamp,
                        qSampleEntity.localDate,
                        qSampleEntity.localDateTime,
                        qSampleEntity.lobText,
                        qSampleEntity.cryptoText
                )).from(qSampleEntity);

        // content
        List<SampleVo> content = query.clone()
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
