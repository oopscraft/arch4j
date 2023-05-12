package org.oopscraft.arch4j.core.variable;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.variable.repository.VariableEntity;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Variable {

    private String id;

    private String value;

    private String name;

    private String note;

    public static Variable from(VariableEntity variableEntity) {
        return Variable.builder()
                .id(variableEntity.getId())
                .value(variableEntity.getValue())
                .name(variableEntity.getName())
                .note(variableEntity.getNote())
                .build();
    }

}
