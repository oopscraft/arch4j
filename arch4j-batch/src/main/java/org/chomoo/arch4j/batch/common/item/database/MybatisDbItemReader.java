package org.chomoo.arch4j.batch.common.item.database;

import org.chomoo.arch4j.core.common.crpyto.CryptoUtil;

/**
 * MybatisCursorItemReader
 * Currently, the Interceptor does not work in MyBatisCursorItemReader,
 * so we directly implement a cursor item reader with a decoding function added.
 * @param <T>
 */
public class MybatisDbItemReader<T> extends org.mybatis.spring.batch.MyBatisCursorItemReader<T> {

    @Override
    protected T doRead() throws Exception {
        T next = super.doRead();
        if(next != null) {
            // decrypt
            CryptoUtil.getInstance().decryptObject(next);
        }
        return next;
    }

}
