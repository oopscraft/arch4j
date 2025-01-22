package org.chomoo.arch4j.core.common.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class ObjectMapperHolder {

    private static ObjectMapper object = null;

    @Autowired(required = false)
    public ObjectMapperHolder(ObjectMapper objectMapper) {
        object = objectMapper;
    }

    public static ObjectMapper getObject() {
        return object;
    }

}
