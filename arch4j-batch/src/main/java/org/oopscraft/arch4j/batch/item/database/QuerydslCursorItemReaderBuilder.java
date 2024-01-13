package org.oopscraft.arch4j.batch.item.database;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.ScrollMode;
import org.oopscraft.arch4j.batch.item.file.DelimitedFileItemReader;
import org.springframework.core.io.Resource;

import javax.persistence.EntityManagerFactory;
import java.util.Optional;

@Setter
@Accessors(chain = true, fluent = true)
public class QuerydslCursorItemReaderBuilder<T> {

    private EntityManagerFactory entityManagerFactory;

    private JPAQuery<T> query;

    private Integer fetchSize;

    private ScrollMode scrollMode;

    public QuerydslCursorItemReader<T> build() {
        QuerydslCursorItemReader<T> instance = new QuerydslCursorItemReader<>();
        Optional.ofNullable(entityManagerFactory).ifPresent(instance::setEntityManagerFactory);
        Optional.ofNullable(query).ifPresent(instance::setQuery);
        Optional.ofNullable(fetchSize).ifPresent(instance::setFetchSize);
        Optional.ofNullable(scrollMode).ifPresent(instance::setScrollMode);
        return instance;
    }

}
