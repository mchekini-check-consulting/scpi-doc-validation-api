package fr.checkconsulting.scpi_doc_validation_api.repository;

import fr.checkconsulting.scpi_doc_validation_api.model.entity.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, String> {
    Optional<UserDocument> findByUserEmailAndType(String email, String type);
}
