package org.oopscraft.arch4j.core.user.repository;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.oopscraft.arch4j.core.data.SystemFieldEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RoleEntity(group of authorities)
 */
@Entity
@Table(name = "role")
@Data
@EqualsAndHashCode(callSuper=false)
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleEntity extends SystemFieldEntity {

    @Id
    @Column(name = "id", length = 64)
    private String id;
    
    @Column(name = "name")
    private String name;

    @Column(name = "note")
    @Lob
    private String note;
    
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
		name = "role_authority",
		joinColumns = @JoinColumn(name = "role_id"), 
		foreignKey = @ForeignKey(name = "none"),
		inverseJoinColumns = @JoinColumn(name = "authority_id"),
		inverseForeignKey = @ForeignKey(name = "none")
	)
    @Builder.Default
	List<AuthorityEntity> authorities = new ArrayList<>();
    
}
