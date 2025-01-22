package org.chomoo.arch4j.core.common.data.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.ZoneId;

@Converter
public class ZoneIdConverter implements AttributeConverter<ZoneId, String> {
	
	@Override
	public String convertToDatabaseColumn(ZoneId attribute) {
        return attribute != null ? attribute.getId() : null;
	}

	@Override
	public ZoneId convertToEntityAttribute(String dbData) {
        return dbData != null ? ZoneId.of(dbData) : null;
	}

}