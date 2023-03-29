package org.oopscraft.arch4j.batch.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.io.Closeable;
import java.io.IOException;

@Slf4j
public class ManualTransactionUtil implements Closeable {

    private final PlatformTransactionManager transactionManager;

    private TransactionStatus transactionStatus;

    public ManualTransactionUtil(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionStatus = transactionManager.getTransaction((TransactionDefinition) defaultTransactionDefinition);
    }

    public void commit() {
        transactionManager.commit(transactionStatus);
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionStatus = transactionManager.getTransaction((TransactionDefinition) defaultTransactionDefinition);
    }

    public void rollback() {
        transactionManager.rollback(transactionStatus);
        DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionStatus = transactionManager.getTransaction((TransactionDefinition) defaultTransactionDefinition);
    }

    @Override
    public void close() {
        try {
            transactionManager.rollback(transactionStatus);
        }catch(Throwable ignore){
            log.warn(ignore.getMessage());
        }
    }
}
