package org.oopscraft.arch4j.core.common.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseModel {

    private boolean systemRequired;

    private LocalDateTime systemUpdatedAt;

    private String systemUpdatedBy;

}
