package com.forum.security;

import com.forum.post.dao.PostRepository;
import com.forum.post.model.Post;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service("webService")
@RequiredArgsConstructor
public class CustomWebSecurity {
	final PostRepository postRepository;
	
	public boolean checkPostAuthor(String postId, String userName) {
		Post post = postRepository.findById(postId).orElse(null);
		return post != null && post.getAuthor().equalsIgnoreCase(userName);
	}
}
