package org.oopscraft.arch4j.core.board.dao;

import org.oopscraft.arch4j.core.board.ContentFormat;
import org.oopscraft.arch4j.core.board.MessageFormat;
import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class MessageFormatConverter extends AbstractEnumConverter<MessageFormat> {

}
