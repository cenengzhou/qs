package com.gammon.pcms.audit;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class PcmsRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		PcmsRevisionEntity newRev = (PcmsRevisionEntity) revisionEntity;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		newRev.setUsername(auth.getName());
	}

}
