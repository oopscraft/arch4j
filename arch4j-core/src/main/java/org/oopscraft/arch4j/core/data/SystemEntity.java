package org.oopscraft.arch4j.core.data;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.converter.BooleanToYNConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(SystemEntityListener.class)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemEntity {

	@Column(name = "system_required", length = 1)
	@Convert(converter= BooleanToYNConverter.class)
	@Builder.Default
	private boolean systemRequired = false;

	@Column(name = "system_updated_at")
	private LocalDateTime systemUpdatedAt;

	@Column(name = "system_updated_by", length = 64)
	private String systemUpdatedBy;

}
