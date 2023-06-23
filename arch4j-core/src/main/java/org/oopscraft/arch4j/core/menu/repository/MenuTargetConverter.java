package org.oopscraft.arch4j.core.menu.repository;

import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.menu.MenuTarget;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class MenuTargetConverter extends AbstractEnumConverter<MenuTarget> {

}
