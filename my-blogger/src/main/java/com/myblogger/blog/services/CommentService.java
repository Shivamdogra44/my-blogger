package com.myblogger.blog.services;

import com.myblogger.blog.payloads.CommentDto;

public interface CommentService {

	
	CommentDto createComment(CommentDto commentDto,Integer postId ,Integer userId);
	void deleteComment(Integer commentId);
}
