package com.myblogger.blog.services.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.myblogger.blog.entities.Category;
import com.myblogger.blog.entities.Post;
import com.myblogger.blog.entities.User;
import com.myblogger.blog.exceptions.ResourceNotFoundException;
import com.myblogger.blog.payloads.PostDto;
import com.myblogger.blog.payloads.PostResponse;
import com.myblogger.blog.repositories.CategoryRepo;
import com.myblogger.blog.repositories.PostRepo;
import com.myblogger.blog.repositories.UserRepo;
import com.myblogger.blog.services.PostService;

@Service
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepo postRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Override
	public PostDto createPost(PostDto postDto,Integer userId,Integer categoryId) {
		
		User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "User id", userId));
		Category category=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "category id", categoryId));
		
		Post post= this.modelMapper.map(postDto, Post.class);
		post.setImageName("default.png");
		post.setAddedDate(new Date());
		post.setUser(user);
		post.setCategory(category);
		
		Post newPost= this.postRepo.save(post);
		
		
		return this.modelMapper.map(newPost, PostDto.class);
	}

	@Override
	public PostDto updatePost(PostDto postDto, Integer postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id", postId));
		post.setTitle(postDto.getTitle());
		post.setContent(postDto.getContent());
		post.setImageName(postDto.getImageName());
		Post updatedPost=this.postRepo.save(post);
		return this.modelMapper.map(updatedPost, PostDto.class);

	}

	@Override
	public void deletePost(Integer postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id", postId));
		this.postRepo.delete(post);
	}

	@Override
	public PostResponse getallPost(Integer pageNumber,Integer pageSize,String sortBy,String sortDir) {
		
		Sort sort=null;
		if(sortDir.equalsIgnoreCase("asc"))
		{
			sort=Sort.by(sortBy).ascending();
		}
		else
		{
			sort=Sort.by(sortBy).descending();
		}
		Pageable p= PageRequest.of(pageNumber, pageSize, sort);  
		Page<Post> allPost = this.postRepo.findAll(p);
		List<PostDto> allPostDto=allPost.stream().map((post)->this.modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
		
		PostResponse postResponse=new PostResponse();
		postResponse.setContent(allPostDto);
		postResponse.setPageNumber(allPost.getNumber());
		postResponse.setSize(allPost.getSize());
		postResponse.setTotalElements(allPost.getTotalElements());
		
		postResponse.setTotalPages(allPost.getTotalPages()); 
		postResponse.setLastPage(allPost.isLast());
		
		return postResponse;
	}

	@Override
	public PostDto getPostById(Integer postId) {
		Post post=this.postRepo.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","Post id", postId));
		return this.modelMapper.map(post,PostDto.class);
		 
	}

	@Override
	public List<PostDto> getPostsByCategory(Integer categoryId) {
		
		Category cat=this.categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","Category id", categoryId));
		
		List<Post> posts=this.postRepo.findBycategory(cat);
		 List<PostDto> postDtos=   posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> getPostsByUser(Integer userId) {
User user=this.userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","user id", userId));
		
		List<Post> posts=this.postRepo.findByUser(user);
		 List<PostDto> postDtos=   posts.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return postDtos;
	}

	@Override
	public List<PostDto> searchPosts(String keyword) {
		List<Post> byTitleContaining = this.postRepo.findByTitleContaining(keyword);
		List<PostDto> collect=byTitleContaining.stream().map((post)->this.modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
		return collect;
	}

}
