package org.oopscraft.arch4j.batch.item.database;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.session.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.managed.ManagedTransactionFactory;
import org.oopscraft.arch4j.core.data.crpyto.CryptoUtil;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * MybatisCursorItemReader
 * @param <T>
 */
@Slf4j
public class MybatisCursorItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> {

    @Setter
    @Getter
    private SqlSessionFactory sqlSessionFactory;

    @Setter
    @Getter
    private DataSource dataSource;

    @Setter
    @Getter
    private Class<?> mapperClass;

    @Setter
    @Getter
    private String mapperMethod;

    @Setter
    @Getter
    private Map<String, Object> parameters;

    private Transaction transaction;

    private Executor executor;

    private SqlSession sqlSession;

    private Cursor<T> cursor;

    private Iterator<T> cursorIterator;

    /**
     * constructor
     */
    public MybatisCursorItemReader() {
        super.setName(ClassUtils.getShortName(this.getClass()));
    }

    @Override
    protected void doOpen() {

        // checks validation
        Assert.notNull(sqlSessionFactory, "sqlSessionFactory must not be null");
        Assert.notNull(dataSource, "dataSource must not be null");
        Assert.notNull(mapperClass, "mapperClass must not be null");
        Assert.notNull(mapperMethod, "mapperMethod must not be null");

        // split springManaged Transaction to managed Transaction
        Configuration configuration = sqlSessionFactory.getConfiguration();
        TransactionFactory transactionFactory = new ManagedTransactionFactory();
        transaction = transactionFactory.newTransaction(dataSource, TransactionIsolationLevel.READ_COMMITTED, false);
        executor = configuration.newExecutor(transaction, ExecutorType.SIMPLE);
        sqlSession = new DefaultSqlSession(configuration, executor, false);
        String statementId = String.format("%s.%s", mapperClass.getName(), mapperMethod);
        cursor = sqlSession.selectCursor(statementId, parameters);
        cursorIterator = cursor.iterator();
    }

    /**
     * jumpToItem
     * @param itemIndex item index
     * @throws Exception exception
     */
    @Override
    protected void jumpToItem(int itemIndex) throws Exception {
        log.info("- {}.jumpToItem({})", this.getClass().getSimpleName(), itemIndex);
        super.jumpToItem(itemIndex);
    }

    /**
     * doRead
     * @return item
     */
    @Override
    protected T doRead() {
        T next = null;
        if (cursorIterator.hasNext()) {
            next = cursorIterator.next();

            // decrypt
            CryptoUtil.getInstance().decryptObject(next);
        }
        return next;
    }

    @Override
    protected void doClose() throws Exception {
        if (cursor != null) {
            cursor.close();
        }
        if(executor != null && !executor.isClosed()){
            executor.close(true);
        }
        if(transaction != null) {
            transaction.close();
        }
        if (sqlSession != null) {
            sqlSession.close();
        }
        cursorIterator = null;
    }


    /**
     * MybatisCursorItemReaderBuilder
     * @param <T>
     */
    @Setter
    @Accessors(chain = true, fluent = true)
    public static class MybatisDbItemReaderBuilder<T> {

        private PlatformTransactionManager transactionManager;

        private SqlSessionFactory sqlSessionFactory;

        private DataSource dataSource;

        private Class<?> mapperClass;

        private String mapperMethod;

        private Map<String, Object> parameters = new LinkedHashMap<>();

        /**
         * parameter
         * @param name parameter value
         * @param value parameter value
         * @return this
         */
        public MybatisDbItemReaderBuilder<T> parameter(String name, Object value) {
            parameters.put(name, value);
            return this;
        }

        /**
         * build
         * @return object
         */
        public MybatisCursorItemReader<T> build() {
            MybatisCursorItemReader<T> reader = new MybatisCursorItemReader<>();
            Optional.ofNullable(sqlSessionFactory).ifPresent(reader::setSqlSessionFactory);
            Optional.ofNullable(dataSource).ifPresent(reader::setDataSource);
            Optional.ofNullable(mapperClass).ifPresent(reader::setMapperClass);
            Optional.ofNullable(mapperMethod).ifPresent(reader::setMapperMethod);
            Optional.ofNullable(parameters).ifPresent(reader::setParameters);
            return reader;
        }
    }

    /**
     * builder
     * @param <T> generic type
     * @return builder
     */
    public static <T> MybatisDbItemReaderBuilder<T> builder() {
        return new MybatisDbItemReaderBuilder<>();
    }
}
