package org.oopscraft.arch4j.core.common.data.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class BooleanConverter implements AttributeConverter<Boolean, String> {
	
	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		if(attribute != null && attribute){
			return "Y";
		}else{
			return "N";
		}
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return "Y".equals(dbData);
	}

}