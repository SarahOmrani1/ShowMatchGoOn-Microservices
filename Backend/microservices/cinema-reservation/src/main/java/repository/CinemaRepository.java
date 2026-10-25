package repository;

import entity.Cinema;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CinemaRepository extends MongoRepository<Cinema, String> {
}
