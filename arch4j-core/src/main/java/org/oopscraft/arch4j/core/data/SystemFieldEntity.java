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
@EntityListeners(SystemFieldListener.class)
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SystemFieldEntity {

	@Column(name = "system_required", length = 1)
	@Convert(converter= BooleanToYNConverter.class)
	@Builder.Default
	private Boolean systemRequired = false;

	@Column(name = "system_update_date_time")
	private LocalDateTime systemUpdateDateTime;

	@Column(name = "system_update_user_id", length = 64)
	private String systemUpdateUserId;

}
