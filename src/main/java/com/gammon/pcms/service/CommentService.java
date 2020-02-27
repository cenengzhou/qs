package com.gammon.pcms.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.dao.CommentRepository;
import com.gammon.pcms.model.Comment;

@Transactional
@Service
public class CommentService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CommentRepository repository;
	
	public List<Comment> obtainQaList(Long idTable, String nameTable, String field) {
		return repository.findByIdTableAndNameTableAndFieldLike(idTable, nameTable, field + "%");
	}
}
