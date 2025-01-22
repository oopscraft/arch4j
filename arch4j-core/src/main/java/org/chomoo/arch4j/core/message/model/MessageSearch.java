package org.chomoo.arch4j.core.message.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MessageSearch {

    private String messageId;

    private String name;

}
