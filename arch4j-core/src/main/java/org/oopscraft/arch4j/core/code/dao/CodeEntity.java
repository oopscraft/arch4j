package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "core_code")
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeEntity extends SystemFieldEntity {
	
	@Id
	@Column(name = "code_id", length = 32)
	private String codeId;
	
	@Column(name = "code_name", length = 128)
	private String codeName;
	
	@Column(name = "note")
	@Lob
	private String note;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(
            name = "code_id",
            referencedColumnName = "code_id",
            updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)
    )
	@OrderBy(CodeItemEntity_.SORT)
	@Builder.Default
    @Setter(AccessLevel.NONE)
	private List<CodeItemEntity> codeItemEntities = new ArrayList<>();

}
