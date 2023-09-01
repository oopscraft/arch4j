package org.oopscraft.arch4j.core.data;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.converter.BooleanToYNConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemFieldModel {

	private boolean systemRequired;

	private LocalDateTime systemUpdatedAt;

	private String systemUpdatedBy;

}
