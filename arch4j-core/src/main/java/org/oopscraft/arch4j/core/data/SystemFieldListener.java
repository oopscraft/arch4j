package org.oopscraft.arch4j.core.data;

import lombok.extern.slf4j.Slf4j;
import org.oopscraft.arch4j.core.security.SecurityUtils;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Slf4j
public class SystemFieldListener {

	@PrePersist
	public void prePersist(SystemFieldEntity entity) {
		entity.setSystemUpdatedAt(LocalDateTime.now());
		entity.setSystemUpdateBy(getCurrentUserId());
		
	}
	
	@PreUpdate
	public void preUpdate(SystemFieldEntity entity) {
		entity.setSystemUpdatedAt(LocalDateTime.now());
		entity.setSystemUpdateBy(getCurrentUserId());
	}

	@PreRemove
	public void preRemove(SystemFieldEntity entity) {
		if(entity.getSystemRequired() != null && entity.getSystemRequired()) {
			throw new RuntimeException("System data can not be deleted.");
		}
	}

	private static String getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
	}

}
