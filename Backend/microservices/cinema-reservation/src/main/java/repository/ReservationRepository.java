package repository;

import entity.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReservationRepository extends MongoRepository<Reservation, String> {
	boolean existsBySeanceIdAndNumeroPlace(String seanceId, String numeroPlace);

	long countBySeanceId(String seanceId);
}
