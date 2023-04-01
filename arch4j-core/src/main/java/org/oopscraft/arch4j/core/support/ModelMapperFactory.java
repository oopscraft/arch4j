package org.oopscraft.arch4j.core.support;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class ModelMapperFactory implements FactoryBean<ModelMapper> {

    public static ModelMapper getInstance() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return modelMapper;
    }

    @Override
    public ModelMapper getObject() throws Exception {
        return getInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return ModelMapper.class;
    }

}
