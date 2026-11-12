package com.exemple.forumpromotion.repository;

import com.exemple.forumpromotion.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}