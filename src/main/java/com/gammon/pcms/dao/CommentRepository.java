package com.gammon.pcms.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gammon.pcms.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{
	List<Comment> findByNameTableAndIdTableAndFieldLikeOrderByDateSentDesc(String nameTable, Long idTable, String field);
}
