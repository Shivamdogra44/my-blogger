package com.myblogger.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myblogger.blog.entities.Category;
import com.myblogger.blog.entities.Post;
import com.myblogger.blog.entities.User;

public interface PostRepo extends JpaRepository<Post, Integer> {

	
	List<Post> findByUser(User user);
	List<Post> findBycategory(Category category);
	List<Post> findByTitleContaining(String titile); 
	
}
