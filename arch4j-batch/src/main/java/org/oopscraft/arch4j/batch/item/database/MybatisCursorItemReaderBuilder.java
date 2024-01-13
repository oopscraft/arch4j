package org.oopscraft.arch4j.batch.item.database;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.SqlSessionFactory;
import org.hibernate.ScrollMode;
import org.mybatis.spring.batch.MyBatisCursorItemReader;
import org.mybatis.spring.batch.builder.MyBatisCursorItemReaderBuilder;

import javax.persistence.EntityManagerFactory;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Setter
@Accessors(chain = true, fluent = true)
public class MybatisCursorItemReaderBuilder<T> {

    private SqlSessionFactory sqlSessionFactory;

    private String queryId;

    private Map<String, Object> parameterValues;

    private Supplier<Map<String, Object>> parameterValuesSupplier;

    private Boolean saveState;

    private Integer maxItemCount;

    public MybatisCursorItemReader<T> build() {
        MybatisCursorItemReader<T> instance = new MybatisCursorItemReader<>();
        Optional.ofNullable(sqlSessionFactory).ifPresent(instance::setSqlSessionFactory);
        Optional.ofNullable(queryId).ifPresent(instance::setQueryId);
        Optional.ofNullable(parameterValues).ifPresent(instance::setParameterValues);
        Optional.ofNullable(parameterValuesSupplier).ifPresent(instance::setParameterValuesSupplier);
        Optional.ofNullable(saveState).ifPresent(instance::setSaveState);
        Optional.ofNullable(maxItemCount).ifPresent(instance::setMaxItemCount);
        return instance;
    }

}
