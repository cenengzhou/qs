package com.gammon.pcms.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gammon.pcms.model.Comment;
import com.gammon.pcms.service.CommentService;

@RestController
@RequestMapping(value = "service/comment/")
public class CommentController {

	@Autowired
	private CommentService service;
	
	@RequestMapping(value = "{nameTable}/{idTable}/{field}", method = RequestMethod.GET)
	public List<Comment> list(@PathVariable String nameTable, @PathVariable Long idTable, @PathVariable String field) {
		return service.obtainCommentList(nameTable, idTable, field);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public Comment post(@RequestBody Comment comment ) {
		return service.save(comment);
	}
}
