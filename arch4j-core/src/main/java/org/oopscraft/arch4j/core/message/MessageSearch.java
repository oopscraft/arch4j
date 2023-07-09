package org.oopscraft.arch4j.core.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageSearch {

    private String messageId;

    private String messageName;

}
