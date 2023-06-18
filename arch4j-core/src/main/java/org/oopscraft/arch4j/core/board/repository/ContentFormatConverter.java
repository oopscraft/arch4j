package org.oopscraft.arch4j.core.board.repository;

import org.oopscraft.arch4j.core.board.TextFormat;
import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class ContentFormatConverter extends AbstractEnumConverter<TextFormat> {

}
