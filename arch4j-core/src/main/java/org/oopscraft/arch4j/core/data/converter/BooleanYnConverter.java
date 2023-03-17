package org.oopscraft.arch4j.core.data.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * jpa converter for boolean to Y/N
 */
@Converter
public class BooleanYnConverter implements AttributeConverter<Boolean, String> {
	
	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		if(attribute != null && attribute == true){
			return "Y";
		}else{
			return "N";
		}
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		return "Y".equals(dbData) ? true : false;
	}

}