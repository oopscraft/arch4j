package org.oopscraft.arch4j.core.sample.entity;

import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.sample.SampleType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class SampleTypeConverter extends AbstractEnumConverter<SampleType> {

}
