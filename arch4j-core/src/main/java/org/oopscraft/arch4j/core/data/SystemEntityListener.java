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
import java.util.Optional;

@Slf4j
public class SystemEntityListener {

	@PrePersist
	public void prePersist(SystemEntity entity) {
		entity.setSystemUpdatedAt(LocalDateTime.now());
		entity.setSystemUpdatedBy(getCurrentUserId());
		
	}
	
	@PreUpdate
	public void preUpdate(SystemEntity entity) {
		entity.setSystemUpdatedAt(LocalDateTime.now());
		entity.setSystemUpdatedBy(getCurrentUserId());
	}

	@PreRemove
	public void preRemove(SystemEntity entity) {
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
