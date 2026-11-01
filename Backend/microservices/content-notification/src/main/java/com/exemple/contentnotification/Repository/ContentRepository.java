package com.exemple.contentnotification.Repository;

import com.exemple.contentnotification.Entity.Content;
import com.exemple.contentnotification.Entity.ContentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ContentRepository extends MongoRepository<Content, String>, ContentRepositoryCustom {
	List<Content> findByStatusAndPublishAtLessThanEqual(ContentStatus status, LocalDateTime dateTime);

	List<Content> findByStatusAndExpireAtLessThanEqual(ContentStatus status, LocalDateTime dateTime);
}
