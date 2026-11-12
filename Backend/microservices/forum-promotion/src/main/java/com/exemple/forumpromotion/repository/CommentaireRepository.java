package com.exemple.forumpromotion.repository;

import com.exemple.forumpromotion.entity.Commentaire;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentaireRepository extends MongoRepository<Commentaire, String> {
}