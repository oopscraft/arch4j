package org.oopscraft.arch4j.core.variable;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.variable.dao.VariableEntity;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Variable {

    private String variableId;

    private String value;

    private String name;

    private String note;

    public static Variable from(VariableEntity variableEntity) {
        return Variable.builder()
                .variableId(variableEntity.getVariableId())
                .value(variableEntity.getValue())
                .name(variableEntity.getName())
                .note(variableEntity.getNote())
                .build();
    }

}
