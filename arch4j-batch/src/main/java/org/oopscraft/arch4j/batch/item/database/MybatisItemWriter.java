package org.oopscraft.arch4j.batch.item.database;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MybatisItemWriter<T> implements ItemStreamWriter<T> {

    @Setter
    @Getter
    private SqlSessionFactory sqlSessionFactory;

    @Setter
    @Getter
    private Class<?> mapperClass;

    @Setter
    @Getter
    private String mapperMethod;

    private SqlSessionTemplate sqlSessionTemplate;

    private final Converter<T, T> itemToParameterConverter =  new Converter<T, T>() {
        @Override
        public T convert(T source) {
            return source;
        }
    };

    /**
     * open
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        // checks validation
        Assert.notNull(sqlSessionFactory, "sqlSessionFactory must not be null");
        Assert.notNull(mapperClass, "mapperClass must not be null");
        Assert.notNull(mapperMethod, "mapperMethod must not be null");

        // creates sqlSessionTemplate
        sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.SIMPLE);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        log.debug("MybatisDbItemWriter.update():{}", executionContext);
    }

    /**
     * write
     * @param items items
     */
    @Override
    public void write(final List<? extends T> items) {
        internalWrite(items);
    }

    /**
     * write
     * @param item items
     */
    public void write(T item) {
        internalWrite(item);
    }

    /**
     * internalWrite
     * @param items items
     */
    protected void internalWrite(final List<? extends T> items) {
        for (T item : items) {
            internalWrite(item);
        }
    }

    /**
     * internalWrite
     * @param item items
     */
    protected void internalWrite(T item) {
        String statementId = String.format("%s.%s", mapperClass.getName(), mapperMethod);
        sqlSessionTemplate.update(statementId, itemToParameterConverter.convert(item));
    }

    /**
     * close
     */
    @Override
    public void close() {
        // closes resources
        if(sqlSessionTemplate != null){
            try {
                sqlSessionTemplate.close();
            } catch(Throwable ignored){}
        }
    }

    @Setter
    @Accessors(chain = true, fluent = true)
    public static class MybatisItemWriterBuilder<T> {

        private SqlSessionFactory sqlSessionFactory;

        private Class<?> mapperClass;

        private String mapperMethod;

        public MybatisItemWriter<T> build() {
            MybatisItemWriter<T> object = new MybatisItemWriter<T>();
            Optional.ofNullable(sqlSessionFactory).ifPresent(object::setSqlSessionFactory);
            Optional.ofNullable(mapperClass).ifPresent(object::setMapperClass);
            Optional.ofNullable(mapperMethod).ifPresent(object::setMapperMethod);
            return object;
        }
    }

    public static <T> MybatisItemWriterBuilder<T> builder() {
        return new MybatisItemWriterBuilder<T>();
    }

}
