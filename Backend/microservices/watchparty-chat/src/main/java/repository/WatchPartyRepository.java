package repository;

import entity.WatchParty;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WatchPartyRepository extends MongoRepository<WatchParty, String> {
}