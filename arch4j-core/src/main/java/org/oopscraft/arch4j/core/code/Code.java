package org.oopscraft.arch4j.core.code;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldSupport;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "code")
@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Code extends SystemFieldSupport {
	
	@Id
	@Column(name = "id", length = 64)
	private String id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "note")
	@Lob
	private String note;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = CodeItem_.CODE_ID, cascade = CascadeType.ALL, orphanRemoval= true)
	@OrderBy(CodeItem_.SORT)
	@Builder.Default
	List<CodeItem> items = new ArrayList<>();


}
