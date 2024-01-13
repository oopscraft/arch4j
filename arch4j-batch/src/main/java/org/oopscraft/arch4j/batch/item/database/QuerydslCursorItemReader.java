package org.oopscraft.arch4j.batch.item.database;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.jpa.QueryHints;
import org.hibernate.query.internal.QueryImpl;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

/**
 * QuerydslCursorItemReader
 * Currently, the Interceptor does not work in MyBatisCursorItemReader,
 * so a decryption function is added after inheritance.
 * @param <T>
 */
@Slf4j
public class QuerydslCursorItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> {

    @Setter
    @Getter
    private EntityManagerFactory entityManagerFactory;

    @Setter
    @Getter
    private JPAQuery<T> query;

    @Setter
    @Getter
    private Integer fetchSize;

    @Setter
    @Getter
    private ScrollMode scrollMode = ScrollMode.FORWARD_ONLY;

    private EntityManager entityManager;

    private ScrollableResults cursor;

    /**
     * constructor
     */
    public QuerydslCursorItemReader() {
        super.setName(ClassUtils.getShortName(this.getClass()));
    }

    @Override
    protected void doOpen() {

        // checks validation
        Assert.notNull(entityManagerFactory, "entityManagerFactory must not be null");
        Assert.notNull(query, "query must not be null");

        // creates cursor
        entityManager = entityManagerFactory.createEntityManager();
        JPAQuery<T> jpaQuery = query.clone(entityManager);
        jpaQuery.setHint(QueryHints.HINT_READONLY, true);
        QueryImpl<?> query = (QueryImpl<?>) jpaQuery.createQuery();
        query.setHint(QueryHints.HINT_READONLY, true);
        query.setReadOnly(true);
        query.setCacheable(false);
        if(fetchSize != null) {
            query.setFetchSize(fetchSize);
        }
        cursor = query.scroll(scrollMode);
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
        if (cursor.next()) {
            Object[] data = cursor.get();
            entityManager.clear();  // Clears in-memory cache
            if(data.length > 1) {
                @SuppressWarnings("unchecked")
                T item = (T) data;
                return item;
            }else{
                @SuppressWarnings("unchecked")
                T item = (T) data[0];
                return item;
            }
        }
        return null;
    }

    /**
     * doClose
     */
    @Override
    protected void doClose() {

        // release resource
        if(cursor != null) {
            cursor.close();
        }
        if(entityManager != null) {
            entityManager.clear();
            entityManager.close();
        }

    }

    /**
     * QuerydslCursorItemReader
     * @param <T>
     */
    @Setter
    @Accessors(chain = true, fluent = true)
    public static class QuerydslCursorItemReaderBuilder<T> {

        private PlatformTransactionManager transactionManager;

        private EntityManagerFactory entityManagerFactory;

        private JPAQuery<T> query;

        private Integer fetchSize;

        private ScrollMode scrollMode;

        /**
         * build
         * @return object
         */
        public QuerydslCursorItemReader<T> build() {
            QuerydslCursorItemReader<T> object = new QuerydslCursorItemReader<>();
            Optional.ofNullable(entityManagerFactory).ifPresent(object::setEntityManagerFactory);
            Optional.ofNullable(query).ifPresent(object::setQuery);
            Optional.ofNullable(fetchSize).ifPresent(object::setFetchSize);
            Optional.ofNullable(scrollMode).ifPresent(object::setScrollMode);
            return object;
        }
    }

    /**
     * builder
     * @param <T> generic type
     * @return builder
     */
    public static <T> QuerydslCursorItemReaderBuilder<T> builder() {
        return new QuerydslCursorItemReaderBuilder<>();
    }

}
