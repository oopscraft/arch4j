package org.chomoo.arch4j.batch.common.item.database;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.ScrollMode;

import jakarta.persistence.EntityManagerFactory;
import java.util.Optional;

@Setter
@Accessors(chain = true, fluent = true)
public class QuerydslDbItemReaderBuilder<T> {

    private EntityManagerFactory entityManagerFactory;

    private JPAQuery<T> query;

    private Integer fetchSize;

    private ScrollMode scrollMode;

    public QuerydslDbItemReader<T> build() {
        QuerydslDbItemReader<T> instance = new QuerydslDbItemReader<>();
        Optional.ofNullable(entityManagerFactory).ifPresent(instance::setEntityManagerFactory);
        Optional.ofNullable(query).ifPresent(instance::setQuery);
        Optional.ofNullable(fetchSize).ifPresent(instance::setFetchSize);
        Optional.ofNullable(scrollMode).ifPresent(instance::setScrollMode);
        return instance;
    }

}
