package repository;

import entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends MongoRepository<Feedback, String> {

	List<Feedback> findByWatchPartyId(String watchPartyId);

	void deleteByWatchPartyId(String watchPartyId);
}