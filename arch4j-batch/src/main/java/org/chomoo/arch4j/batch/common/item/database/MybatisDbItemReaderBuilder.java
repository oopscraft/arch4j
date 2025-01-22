package org.chomoo.arch4j.batch.common.item.database;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Setter
@Accessors(chain = true, fluent = true)
public class MybatisDbItemReaderBuilder<T> {

    private SqlSessionFactory sqlSessionFactory;

    private String queryId;

    private Map<String, Object> parameterValues;

    private Supplier<Map<String, Object>> parameterValuesSupplier;

    private Boolean saveState;

    private Integer maxItemCount;

    public MybatisDbItemReader<T> build() {
        MybatisDbItemReader<T> instance = new MybatisDbItemReader<>();
        Optional.ofNullable(sqlSessionFactory).ifPresent(instance::setSqlSessionFactory);
        Optional.ofNullable(queryId).ifPresent(instance::setQueryId);
        Optional.ofNullable(parameterValues).ifPresent(instance::setParameterValues);
        Optional.ofNullable(parameterValuesSupplier).ifPresent(instance::setParameterValuesSupplier);
        Optional.ofNullable(saveState).ifPresent(instance::setSaveState);
        Optional.ofNullable(maxItemCount).ifPresent(instance::setMaxItemCount);
        return instance;
    }

}
