package org.chomoo.arch4j.core.common.support;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
public class TransactionTemplateUtil {

    private final PlatformTransactionManager transactionManager;

    public static void executeWithoutResult(PlatformTransactionManager transactionManager, Propagation propagation, Consumer<TransactionStatus> consumer) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(propagation.value());
        transactionDefinition.setIsolationLevel(Isolation.READ_UNCOMMITTED.value());
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        transactionTemplate.executeWithoutResult(consumer);
    }

    public void executeWithoutResult(Propagation propagation, Consumer<TransactionStatus> consumer) {
        executeWithoutResult(transactionManager, propagation, consumer);
    }

    public static <T> T execute(PlatformTransactionManager transactionManager, Propagation propagation, TransactionCallback<T> transactionCallback) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setPropagationBehavior(propagation.value());
        transactionDefinition.setIsolationLevel(Isolation.READ_UNCOMMITTED.value());
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager, transactionDefinition);
        return transactionTemplate.execute(transactionCallback);
    }

    public <T> T execute(Propagation propagation, TransactionCallback<T> transactionCallback) {
        return execute(transactionManager, propagation, transactionCallback);
    }

}
