package com.forum.post.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.forum.post.dto.DatePeriodDto;
import com.forum.post.dto.NewCommentDto;
import com.forum.post.dto.NewPostDto;
import com.forum.post.dto.PostDto;
import com.forum.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forum")
public class PostController {

    final PostService postService;

    private String formatPostDto(PostDto postDto) {
        StringBuilder sb = new StringBuilder();
        sb.append("Post ID: ").append(postDto.getId()).append("\n");
        sb.append("Title: ").append(postDto.getTitle()).append("\n");
        sb.append("Content: ").append(postDto.getContent()).append("\n");
        sb.append("Author: ").append(postDto.getAuthor()).append("\n");
        sb.append("Date Created: ").append(postDto.getDateCreated()).append("\n");
        sb.append("Likes: ").append(postDto.getLikes()).append("\n");
        sb.append("Tags: ").append(postDto.getTags()).append("\n");
        sb.append("Comments: ").append(postDto.getComments()).append("\n");

        return sb.toString();
    }

    @PostMapping("/post/{author}")
    public PostDto addNewPost(@PathVariable String author, @RequestBody NewPostDto newPostDto) {
        return postService.addNewPost(author, newPostDto);
    }

    @GetMapping("/post/{id}")
    public PostDto findPostById(@PathVariable String id) {
        return postService.findPostById(id);
    }

    @DeleteMapping("/post/{id}")
    public PostDto removePost(@PathVariable String id) {
        return postService.removePost(id);
    }

    @PutMapping("/post/{id}")
    public PostDto updatePost(@PathVariable String id, @RequestBody NewPostDto newPostDto) {
        return postService.updatePost(id, newPostDto);
    }

    @PutMapping("/post/{id}/comment/{author}")
    public PostDto addComment(@PathVariable String id, @PathVariable String author,
                              @RequestBody NewCommentDto newCommentDto) {
        return postService.addComment(id, author, newCommentDto);
    }

    @DeleteMapping("/post/{id}/comment")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeComment(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        postService.removeComment(id, username);
    }

    @PutMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        postService.addLike(id, username);
    }

    @DeleteMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        postService.removeLike(id, username);
    }

    @GetMapping("/posts/author/{author}")
    public Iterable<PostDto> findPostsByAuthor(@PathVariable String author) {
        return postService.findPostsByAuthor(author);
    }

    @PostMapping("/posts/tags")
    public Iterable<PostDto> findPostsByTags(@RequestBody List<String> tags) {
        return postService.findPostsByTags(tags);
    }

    @PostMapping("/posts/period")
    public Iterable<PostDto> findPostsByPeriod(@RequestBody DatePeriodDto datePeriodDto) {
        return postService.findPostsByPeriod(datePeriodDto);
    }
}