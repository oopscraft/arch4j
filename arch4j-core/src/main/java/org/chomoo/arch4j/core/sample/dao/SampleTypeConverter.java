package org.chomoo.arch4j.core.sample.dao;

import org.chomoo.arch4j.core.common.data.converter.AbstractEnumConverter;
import org.chomoo.arch4j.core.sample.model.SampleType;

import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SampleTypeConverter extends AbstractEnumConverter<SampleType> {

}
