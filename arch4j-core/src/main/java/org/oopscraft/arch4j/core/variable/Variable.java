package org.oopscraft.arch4j.core.variable;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.BaseModel;
import org.oopscraft.arch4j.core.variable.dao.VariableEntity;

@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Variable extends BaseModel {

    private String variableId;

    private String variableName;

    private String value;

    private String note;

    public static Variable from(VariableEntity variableEntity) {
        return Variable.builder()
                .systemRequired(variableEntity.isSystemRequired())
                .systemUpdatedAt(variableEntity.getSystemUpdatedAt())
                .systemUpdatedBy(variableEntity.getSystemUpdatedBy())
                .variableId(variableEntity.getVariableId())
                .value(variableEntity.getValue())
                .variableName(variableEntity.getVariableName())
                .note(variableEntity.getNote())
                .build();
    }

}
