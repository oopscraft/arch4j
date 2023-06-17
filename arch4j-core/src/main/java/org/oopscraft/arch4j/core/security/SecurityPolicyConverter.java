package org.oopscraft.arch4j.core.security;

import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class SecurityPolicyConverter extends AbstractEnumConverter<SecurityPolicy> {

}
