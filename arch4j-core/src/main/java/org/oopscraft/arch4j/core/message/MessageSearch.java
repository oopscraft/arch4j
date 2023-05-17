package org.oopscraft.arch4j.core.message;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class MessageSearch {

    private String id;

    private String name;

}
