package org.oopscraft.arch4j.core.data.converter;

import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.data.crpyto.CryptoUtil;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;
import javax.persistence.Converter;

@Converter
@Slf4j
public class CryptoConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        String dbData = CryptoUtil.getInstance().encrypt(attribute);
        log.trace("convertToDatabaseColumn:{} -> {}", attribute, dbData);
        return dbData;
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        String attribute = CryptoUtil.getInstance().decrypt(dbData);
        log.trace("convertToEntityAttribute:{} -> {}", dbData, attribute);
        return attribute;
    }

}