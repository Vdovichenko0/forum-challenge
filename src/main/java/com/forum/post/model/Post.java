package com.forum.post.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@Document(collection = "posts")
public class Post {
    String id;
    @Setter
    String title;
    @Setter
    String content;
    @Setter
    String author;
    LocalDateTime dateCreated = LocalDateTime.now();
    Set<String> tags = new HashSet<>();
    int likes;
    List<Comment> comments = new ArrayList<>();
    Set<String> likedBy = new HashSet<>();

    public Post(String title, String content, String author, Set<String> tags) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.tags = tags;
    }

    public boolean addLike(String user) {
        if (likedBy.add(user)) {
            likes++;
            return true;
        }
        return false;
    }

    public boolean removeLike(String user) {
        if (likedBy.remove(user)) {
            likes--;
            return true;
        }
        return false;
    }

    public boolean removeComment(String user) {
        Comment commentToRemove = null;
        for (Comment comment : comments) {
            if (comment.getUser().equals(user)) {
                commentToRemove = comment;
                break;
            }
        }
        if (commentToRemove != null) {
            comments.remove(commentToRemove);
            return true;
        }
        return false;
    }

    public boolean addTag(String tag) {
        return tags.add(tag);
    }

    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    public boolean addComment(Comment comment) {
        return comments.add(comment);
    }


}

