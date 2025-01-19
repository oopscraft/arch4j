package org.oopscraft.arch4j.core.common.data;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.common.data.converter.BooleanConverter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity {

	@Column(name = "system_required", length = 1)
	@Convert(converter= BooleanConverter.class)
	@Builder.Default
	private boolean systemRequired = false;

	@Column(name = "system_updated_at")
	private LocalDateTime systemUpdatedAt;

	@Column(name = "system_updated_by", length = 64)
	private String systemUpdatedBy;

}
