package com.forum.post.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;
import com.forum.post.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
    Stream<Post> findByAuthorIgnoreCase(String author);

    Stream<Post> findByTagsInIgnoreCase(List<String> tags);

    Stream<Post> findByDateCreatedBetween(LocalDate from, LocalDate to);


}
