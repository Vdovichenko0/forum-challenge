package com.forum.post.service;

import com.forum.post.dto.DatePeriodDto;
import com.forum.post.dto.NewCommentDto;
import com.forum.post.dto.NewPostDto;
import com.forum.post.dto.PostDto;

import java.util.List;

public interface PostService {
    PostDto addNewPost(String author, NewPostDto newPostDto);

    PostDto findPostById(String id);

    PostDto removePost(String id);

    PostDto updatePost(String id, NewPostDto newPostDto);

    PostDto addComment(String id, String author, NewCommentDto newCommentDto);

    void removeComment(String id, String user);

    void addLike(String id, String user);

    void removeLike(String id, String user);

    Iterable<PostDto> findPostsByAuthor(String author);

    Iterable<PostDto> findPostsByTags(List<String> tags);

    Iterable<PostDto> findPostsByPeriod(DatePeriodDto datePeriodDto);

}

