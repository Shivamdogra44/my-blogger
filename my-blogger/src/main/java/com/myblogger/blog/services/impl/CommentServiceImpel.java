package com.myblogger.blog.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myblogger.blog.entities.Comment;
import com.myblogger.blog.entities.Post;
import com.myblogger.blog.entities.User;
import com.myblogger.blog.exceptions.ResourceNotFoundException;
import com.myblogger.blog.payloads.CommentDto;
import com.myblogger.blog.repositories.CommentRepo;
import com.myblogger.blog.repositories.PostRepo;
import com.myblogger.blog.repositories.UserRepo;
import com.myblogger.blog.services.CommentService;

@Service
public class CommentServiceImpel implements CommentService {

	@Autowired
	private PostRepo postRepo;
	@Autowired
	private CommentRepo commentRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public CommentDto createComment(CommentDto commentDto, Integer postId,Integer userId) {
			
		
		Post post=this.postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post", "postId", postId));
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "userId", postId)); 
		commentDto.setUserId(userId);
		Comment comment=this.modelMapper.map(commentDto,Comment.class);
		
		comment.setPost(post);
		comment.setUser(user);
		Comment savedComment=this.commentRepo.save(comment);
		

		return this.modelMapper.map(savedComment, CommentDto.class);
	}

	@Override
	public void deleteComment(Integer commentId) {
		Comment comment=this.commentRepo.findById(commentId).orElseThrow(()->new ResourceNotFoundException("Comment", "commentId", commentId));
		this.commentRepo.delete(comment);

	}

}
