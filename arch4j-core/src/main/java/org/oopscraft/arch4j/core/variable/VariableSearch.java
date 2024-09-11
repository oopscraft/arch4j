package org.oopscraft.arch4j.core.variable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VariableSearch {

    private String variableId;

    private String name;

}
