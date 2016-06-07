
package com.gammon.pcms.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.gammon.pcms.model.SpringSession;

public interface SpringSessionSPDao extends CrudRepository<SpringSession, Long> {
	void deleteBySessionId(String sessionId);
	SpringSession findBySessionId(String sessionId);
	List<SpringSession> findByPrincipalName(String principalName);
}
