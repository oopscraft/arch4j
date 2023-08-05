package org.oopscraft.arch4j.core.code.dao;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "core_code_item")
@IdClass(CodeItemEntity.Pk.class)
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CodeItemEntity extends SystemFieldEntity {

	@Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
	public static class Pk implements Serializable {
		private String codeId;
		private String itemId;
	}

	@Id
	@Column(name = "code_id", length = 32)
	private String codeId;
	
	@Id
	@Column(name = "item_id", length = 32)
	private String itemId;

    @Column(name = "item_name", length = 128)
    private String itemName;

    @Column(name = "sort")
	private Integer sort;

}
