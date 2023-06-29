package org.oopscraft.arch4j.core.user.dao;

import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.user.UserType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserTypeConverter extends AbstractEnumConverter<UserType> {

}
