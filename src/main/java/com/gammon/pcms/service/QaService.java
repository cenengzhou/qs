package com.gammon.pcms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.QaRepository;
import com.gammon.pcms.model.Qa;

@Transactional
@Service
public class QaService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private QaRepository repository;
	
	public List<Qa> obtainQaList(Long idTable, String nameTable, String field) {
		return repository.findByIdTableAndNameTableAndFieldLike(idTable, nameTable, field + "%");
	}
}
