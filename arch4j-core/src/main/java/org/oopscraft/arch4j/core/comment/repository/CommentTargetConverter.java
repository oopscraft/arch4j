package org.oopscraft.arch4j.core.comment.repository;

import org.oopscraft.arch4j.core.comment.CommentTarget;
import org.oopscraft.arch4j.core.data.converter.AbstractEnumConverter;
import org.oopscraft.arch4j.core.user.UserType;

import javax.persistence.Converter;

@Converter(autoApply = true)
public class CommentTargetConverter extends AbstractEnumConverter<CommentTarget> {

}
