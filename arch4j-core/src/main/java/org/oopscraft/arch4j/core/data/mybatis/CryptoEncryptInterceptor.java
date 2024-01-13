package org.oopscraft.arch4j.core.data.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.oopscraft.arch4j.core.data.crpyto.Crypto;
import org.oopscraft.arch4j.core.data.crpyto.CryptoUtil;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Slf4j
@Component
@Intercepts({@Signature(
        type = Executor.class,
        method = "update",
        args = {MappedStatement.class, Object.class}
)})
public class CryptoEncryptInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if(args.length >= 2) {
           Object parameter = args[1];
           CryptoUtil.getInstance().encryptObject(parameter);
        }
        Object returnObject = invocation.proceed();
        log.debug("returnObject:{}", returnObject);
        return returnObject;
    }

}
