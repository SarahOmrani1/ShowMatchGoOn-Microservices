package repository;

import entity.Commentaire;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentaireRepository extends MongoRepository<Commentaire, String> {
}