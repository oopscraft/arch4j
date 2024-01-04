package org.oopscraft.arch4j.core.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Slf4j
public class BaseEntityListener {

	@PrePersist
	public void prePersist(BaseEntity entity) {
		entity.setSystemUpdatedAt(LocalDateTime.now());
		entity.setSystemUpdatedBy(getCurrentUserId());
		
	}
	
	@PreUpdate
	public void preUpdate(BaseEntity entity) {
		entity.setSystemUpdatedAt(LocalDateTime.now());
		entity.setSystemUpdatedBy(getCurrentUserId());
	}

	@PreRemove
	public void preRemove(BaseEntity entity) {
		if(entity.isSystemRequired()) {
			throw new RuntimeException("System data can not be deleted.");
		}
	}

    private static String getCurrentUserId() {
        String userId = null;
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if(securityContext != null) {
            Authentication authentication = securityContext.getAuthentication();
            if(authentication instanceof UsernamePasswordAuthenticationToken) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                userId = userDetails.getUsername();
            }
        }
        return userId;
    }

}
