package org.chomoo.arch4j.batch.common.item.database;

import com.querydsl.jpa.impl.JPAQuery;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.jpa.QueryHints;
//import org.hibernate.query.internal.QueryImpl;
import org.hibernate.query.Query;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

/**
 * QuerydslCursorItemReader
 * Currently, the Interceptor does not work in MyBatisCursorItemReader,
 * so a decryption function is added after inheritance.
 * @param <T>
 */
@Slf4j
public class QuerydslDbItemReader<T> extends AbstractItemCountingItemStreamItemReader<T> {

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

    private ScrollableResults<T> cursor;

    public QuerydslDbItemReader() {
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
//        jpaQuery.setHint(QueryHints.HINT_READONLY, true);
        Query<T> query = (Query<T>) jpaQuery.createQuery();
//        query.setHint(QueryHints.HINT_READONLY, true);
        query.setReadOnly(true);
        query.setCacheable(false);
        if(fetchSize != null) {
            query.setFetchSize(fetchSize);
        }
        cursor = query.scroll(scrollMode);
    }

    @Override
    protected void jumpToItem(int itemIndex) throws Exception {
        log.info("- {}.jumpToItem({})", this.getClass().getSimpleName(), itemIndex);
        super.jumpToItem(itemIndex);
    }

    @Override
    protected T doRead() {
        if (cursor.next()) {
            Object[] data = (Object[]) cursor.get();
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

}
