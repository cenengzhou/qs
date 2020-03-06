package com.gammon.pcms.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gammon.pcms.application.User;
import com.gammon.pcms.dao.CommentRepository;
import com.gammon.pcms.model.Comment;
import com.gammon.pcms.model.hr.HrUser;
import com.gammon.qs.service.security.SecurityService;

@Transactional
@Service
public class CommentService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CommentRepository repository;
	@Autowired
	private HrService hrService;
	@Autowired
	private SecurityService securityService;
	
	public List<Comment> obtainCommentList(String nameTable, Long idTable, String field) {
		List<Comment> commentList = repository.findByNameTableAndIdTableAndFieldLikeOrderByDateSentDesc(nameTable, idTable, field + "%");
		return SenderObject(commentList);
	}
	
	public Comment save(Comment comment) {
		User currentUser = securityService.getCurrentUser();
		comment.setDateSent(new Date());
		comment.setSender(currentUser.getUsername());
		return repository.save(comment);
	}
	
	public List<Comment> SenderObject(List<Comment> comments) {
		Map<String, HrUser> userMap = new HashMap<>();
		comments.forEach(fieldComment -> {
			HrUser user = userMap.get(fieldComment.getSender());
			if(user == null) {
				user = hrService.findByUsername(fieldComment.getSender());
				userMap.put(fieldComment.getSender(), user);
			}
			fieldComment.setSenderObject(user);
		});
		return comments;
	}
}
