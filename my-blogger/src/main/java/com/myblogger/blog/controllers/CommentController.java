package com.myblogger.blog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myblogger.blog.entities.Comment;
import com.myblogger.blog.payloads.ApiResponse;
import com.myblogger.blog.payloads.CommentDto;
import com.myblogger.blog.services.CommentService;

@RestController
@RequestMapping("/api")
public class CommentController {
	@Autowired
	private CommentService commentService;
	
	@PostMapping("/post/{postId}/user/{userId}/comments")
	public ResponseEntity<CommentDto > createComment(@RequestBody CommentDto comment,@PathVariable Integer postId,@PathVariable Integer userId)
	{
				CommentDto commentDto=this.commentService.createComment(comment, postId, userId);
				return new ResponseEntity<CommentDto>(commentDto,HttpStatus.CREATED);
	}
	
	@DeleteMapping("/comment/{commentId}")
	public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId)
	{
		this.commentService.deleteComment(commentId);
		return new ResponseEntity<ApiResponse>(new ApiResponse("Comment Deleted!!",true),HttpStatus.OK);
	}
	
}
