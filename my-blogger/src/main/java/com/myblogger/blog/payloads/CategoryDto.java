package com.myblogger.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

	private Integer categoryId;
	@NotBlank
	@Size(min=4,message="Category Title cannot be less than 4 characters!!")
	private String categoryTitle;
	@NotBlank
	@Size(min=10,message="Category Description cannot be less than 10 characters!!")
	private String categoryDescription;
}
