package org.chomoo.arch4j.core.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.chomoo.arch4j.core.common.data.BaseModel;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMfa extends BaseModel {

    private String userId;

    private Type type;

    private boolean enabled;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String secret;

    public enum Type { TOTP, EMAIL }

}
