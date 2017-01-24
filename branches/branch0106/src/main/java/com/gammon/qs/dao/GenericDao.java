package com.gammon.qs.dao;

import java.util.List;

import com.gammon.qs.application.exception.DatabaseOperationException;

public interface GenericDao <T> {
	void insert(T newInstance) throws DatabaseOperationException;
	void saveOrUpdate(T newInstance) throws DatabaseOperationException;
	void update(T existingInstance) throws DatabaseOperationException;
	void updateFlushClear(T existingInstance) throws DatabaseOperationException;
	T get(Long id) throws DatabaseOperationException;
	List<T> getAll() throws DatabaseOperationException;
	List<T> getAllActive() throws DatabaseOperationException;
	void delete(T persistentObject) throws DatabaseOperationException;
	void deleteById(Long id) throws DatabaseOperationException;
	void inactivate(T persistentObject) throws DatabaseOperationException;
	void inactivateById(Long id) throws DatabaseOperationException;
	
}
