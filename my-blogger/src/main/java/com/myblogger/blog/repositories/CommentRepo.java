package com.myblogger.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myblogger.blog.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer>{

	
	
	
}
