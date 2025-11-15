package fr.checkconsulting.scpi_doc_validation_api.repository;

import fr.checkconsulting.scpi_doc_validation_api.model.entity.UserDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDocumentRepository extends MongoRepository<UserDocument, String> {

    List<UserDocument> findByUserEmail(String userEmail);

}
