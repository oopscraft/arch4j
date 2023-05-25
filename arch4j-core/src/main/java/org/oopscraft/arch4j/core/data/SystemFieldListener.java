package org.oopscraft.arch4j.core.data;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Slf4j
public class SystemFieldListener {

	@PrePersist
	public void prePersist(SystemFieldEntity entity) {
		entity.setSystemUpdateDateTime(LocalDateTime.now());
		entity.setSystemUpdateUserId(getCurrentUserId());
		
	}
	
	@PreUpdate
	public void preUpdate(SystemFieldEntity entity) {
		entity.setSystemUpdateDateTime(LocalDateTime.now());
		entity.setSystemUpdateUserId(getCurrentUserId());
	}

	@PreRemove
	public void preRemove(SystemFieldEntity entity) {
		if(entity.getSystemRequired() != null && entity.getSystemRequired()) {
			throw new RuntimeException("System data can not be deleted.");
		}
	}

	/*
	 * Return login user id
	 * @return
	 * @throws Exception
	 */
	private static final String getCurrentUserId() {
		// TODO
		return null;
	}

}
