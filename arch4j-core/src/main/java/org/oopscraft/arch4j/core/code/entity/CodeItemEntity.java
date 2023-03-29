package org.oopscraft.arch4j.core.code.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "code_item")
@IdClass(CodeItemEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemEntity extends SystemFieldEntity {

	@Data
	public static class Pk implements Serializable {
		private String codeId;
		private String id;
	}

	@Id
	@Column(name = "code_id", length = 64)
	private String codeId;
	
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "sort")
	private int sort;
	
	@Column(name = "name")
	private String name;

    @Column(name = "value")
    private String value;
	
}
