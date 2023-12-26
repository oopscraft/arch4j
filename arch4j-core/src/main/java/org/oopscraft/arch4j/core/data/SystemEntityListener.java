package org.oopscraft.arch4j.core.data;

import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.security.SecurityUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Slf4j
public class SystemEntityListener {

	@PrePersist
	public void prePersist(SystemEntity entity) {
		entity.setSystemUpdatedAt(LocalDateTime.now());
		entity.setSystemUpdatedBy(SecurityUtils.getCurrentUserId());
		
	}
	
	@PreUpdate
	public void preUpdate(SystemEntity entity) {
		entity.setSystemUpdatedAt(LocalDateTime.now());
		entity.setSystemUpdatedBy(SecurityUtils.getCurrentUserId());
	}

	@PreRemove
	public void preRemove(SystemEntity entity) {
		if(entity.isSystemRequired()) {
			throw new RuntimeException("System data can not be deleted.");
		}
	}

}
