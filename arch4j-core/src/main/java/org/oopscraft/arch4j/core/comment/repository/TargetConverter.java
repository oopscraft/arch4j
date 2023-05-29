package org.oopscraft.arch4j.core.comment.repository;

import org.oopscraft.arch4j.core.comment.TargetType;
import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class TargetConverter extends AbstractEnumConverter<TargetType> {

}
