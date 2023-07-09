package org.oopscraft.arch4j.core.variable.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;

@Entity
@Table(name = "core_variable")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class VariableEntity extends SystemFieldEntity {

    @Id
    @Column(name = "variable_id", length = 32)
    private String variableId;

    @Column(name = "variable_name")
    private String variableName;

    @Column(name = "value", length = 1024)
    private String value;

    @Column(name = "note")
    @Lob
    private String note;

}
