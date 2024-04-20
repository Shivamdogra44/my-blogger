package com.myblogger.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.myblogger.blog.entities.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{
	

}
