package org.oopscraft.arch4j.core.data.converter;

import org.oopscraft.arch4j.core.data.crpyto.CryptoUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class CryptoConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return CryptoUtil.getInstance().encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return CryptoUtil.getInstance().decrypt(dbData);
    }

}