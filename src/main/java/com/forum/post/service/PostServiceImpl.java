package com.forum.post.service;

import java.util.List;
import java.util.Set;

import com.forum.post.dao.PostRepository;
import com.forum.post.dto.DatePeriodDto;
import com.forum.post.dto.NewCommentDto;
import com.forum.post.dto.NewPostDto;
import com.forum.post.dto.PostDto;
import com.forum.post.dto.exceptions.CharacterCountException;
import com.forum.post.dto.exceptions.PostNotFoundException;
import com.forum.post.model.Comment;
import com.forum.post.model.Post;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

	final PostRepository postRepository;
	final ModelMapper modelMapper;

	@Override
	public PostDto addNewPost(String author, NewPostDto newPostDto) {
		if(newPostDto.getContent().length()>1000 || newPostDto.getTitle().length()>100){
			throw new CharacterCountException();
		}
		Post post = modelMapper.map(newPostDto, Post.class);
		post.setAuthor(author);
		post = postRepository.save(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto findPostById(String id) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto removePost(String id) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		postRepository.delete(post);
		return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto updatePost(String id, NewPostDto newPostDto) {
		if(newPostDto.getContent().length()>1000 || newPostDto.getTitle().length()>100){
			throw new CharacterCountException();
		}
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        String content = newPostDto.getContent();
        if (content != null) {
            post.setContent(content);
        }
        String title = newPostDto.getTitle();
        if (title != null) {
            post.setTitle(title);
        }
        Set<String> tags = newPostDto.getTags();
        if (tags != null) {
        	tags.forEach(post::addTag);
        }
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
	}

	@Override
	public PostDto addComment(String id, String author, NewCommentDto newCommentDto) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        Comment comment = new Comment(author, newCommentDto.getMessage());
        post.addComment(comment);
        post = postRepository.save(post);
        return modelMapper.map(post, PostDto.class);
	}

	@Override
	public void removeComment(String id, String user) {
		Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
		if (post.removeComment(user)) {
			postRepository.save(post);
		} else {
			throw new AccessDeniedException("Access denied to remove comment.");
		}
	}

	@Override
	public void addLike(String postId, String user) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		if (post.addLike(user)) {
			postRepository.save(post);
		} else {
			throw new AccessDeniedException("You liked this post.");
		}
	}

	@Override
	public void removeLike(String postId, String user) {
		Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
		if (post.removeLike(user)) {
			postRepository.save(post);
		} else {
			throw new AccessDeniedException("You not liked this post.");
		}
	}

	@Override
	public Iterable<PostDto> findPostsByAuthor(String author) {
		return postRepository.findByAuthorIgnoreCase(author)
				.map(p -> modelMapper.map(p, PostDto.class))
				.toList();
	}

	@Override
	public Iterable<PostDto> findPostsByTags(List<String> tags) {
		return postRepository.findByTagsInIgnoreCase(tags)
				.map(p -> modelMapper.map(p, PostDto.class))
				.toList();
	}

	@Override
	public Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto) {
		return postRepository.findByDateCreatedBetween(datePeriodDto.getDateFrom(), datePeriodDto.getDateTo())
				.map(p -> modelMapper.map(p, PostDto.class))
				.toList();
	}

}
