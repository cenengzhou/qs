
package com.gammon.pcms.dao;

import org.springframework.data.repository.CrudRepository;

import com.gammon.pcms.model.SpringSession;

public interface SpringSessionSPDao extends CrudRepository<SpringSession, Long> {
	void deleteBySessionId(String sessionId);
}
