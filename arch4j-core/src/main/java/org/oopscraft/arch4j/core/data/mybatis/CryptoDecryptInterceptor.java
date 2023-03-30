package org.oopscraft.arch4j.core.data.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.oopscraft.arch4j.core.data.crpyto.CryptoUtil;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;

@Slf4j
@Component
@Intercepts({@Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {
                Statement.class
        }
)})
public class CryptoDecryptInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {

//        Object[] args = invocation.getArgs();
//        for(int i = 1; i < args.length; i ++ ) {
//            Object object = args[i];
//            decryptField(object);
//        }
        Object returnObject = invocation.proceed();
        log.debug("== returnObject:{}", returnObject);
        return returnObject;
    }

//    private void decryptField(Object object) throws IllegalAccessException {
//        Field[] fields = object.getClass().getDeclaredFields();
//        for(Field field : fields) {
//            Annotation annotation = field.getAnnotation(Crypto.class);
//            if(annotation != null) {
//                field.setAccessible(true);
//                String rawText = (String) field.get(object);
//                String cipherText = CryptoUtil.getInstance().encrypt(rawText);
//                field.set(object, cipherText);
//            }
//        }
//    }
}
