package org.chomoo.arch4j.core.common.crpyto.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.chomoo.arch4j.core.common.crpyto.CryptoUtil;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class CryptoDecryptInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        log.debug("intercept:{}", invocation);

        // encrypt parameter
        Object[] args = invocation.getArgs();
        if(args.length >= 2) {
            Object parameter = args[1];
            if (parameter != null) {
                CryptoUtil.getInstance().encryptObject(parameter);
            }
        }

        // proceed
        Object returnObject = invocation.proceed();

        // decrypt result
        if(returnObject instanceof List) {
            List<Object> list = (List<Object>) returnObject;
            CryptoUtil cryptoUtil = CryptoUtil.getInstance();
            list.forEach(cryptoUtil::decryptObject);
        }

        // return
        return returnObject;
    }

}
