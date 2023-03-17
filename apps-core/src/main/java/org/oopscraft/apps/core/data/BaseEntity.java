package org.oopscraft.apps.core.data;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.apps.core.data.converter.BooleanYnConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(BaseEntityListener.class)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseEntity {

	@Column(name = "system_data_yn", length = 1)
	@Convert(converter= BooleanYnConverter.class)
	@Builder.Default
	private Boolean systemData = false;

	@Column(name = "system_modify_date_time")
	private LocalDateTime systemModifyDateTime;

	@Column(name = "system_modify_user_id", length = 64)
	private String systemModifyUserId;

}
