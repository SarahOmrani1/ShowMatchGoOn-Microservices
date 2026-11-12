package com.exemple.forumpromotion.repository;

import com.exemple.forumpromotion.entity.Promotion;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PromotionRepository extends MongoRepository<Promotion, String> {
}