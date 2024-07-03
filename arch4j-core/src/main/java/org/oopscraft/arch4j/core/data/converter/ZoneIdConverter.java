package org.oopscraft.arch4j.core.data.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
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