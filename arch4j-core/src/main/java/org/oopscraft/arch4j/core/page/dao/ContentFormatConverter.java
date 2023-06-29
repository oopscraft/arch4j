package org.oopscraft.arch4j.core.page.dao;

import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.page.ContentFormat;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ContentFormatConverter extends AbstractEnumConverter<ContentFormat> {

}
