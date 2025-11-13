package fr.checkconsulting.scpi_doc_validation_api.service;

import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import fr.checkconsulting.scpi_doc_validation_api.mapper.UserDocumentMapper;
import fr.checkconsulting.scpi_doc_validation_api.model.entity.UserDocument;
import fr.checkconsulting.scpi_doc_validation_api.repository.UserDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static fr.checkconsulting.scpi_doc_validation_api.model.enums.DocumentStatus.UNDER_REVIEW;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final UserDocumentRepository userDocumentRepository;
    private final UserDocumentMapper userDocumentMapper;

    public DocumentService(UserDocumentRepository userDocumentRepository, UserDocumentMapper userDocumentMapper) {
        this.userDocumentRepository = userDocumentRepository;
        this.userDocumentMapper = userDocumentMapper;
    }

    public void saveDocument(UserDocumentDto dto) {
        if (dto == null) {
            logger.info(" Le document reçu est null, sauvegarde ignorée.");
            return;
        }

        UserDocument entity = UserDocument.builder()
                .userEmail(dto.getUserEmail())
                .fullName(dto.getFullName())
                .type(dto.getType())
                .status(UNDER_REVIEW)
                .originalFileName(dto.getOriginalFileName())
                .storedFileName(dto.getStoredFileName())
                .bucketName(dto.getBucketName())
                .uploadedAt(dto.getUploadedAt())
                .lastUpdatedAt(dto.getLastUpdatedAt())
                .build();

        userDocumentRepository.save(entity);
        logger.info(" Document sauvegardé dans MongoDB pour l'utilisateur {}", dto.getUserEmail());
    }

    public List<UserDocumentDto> findAllDocuments() {
        List<UserDocument> documents = userDocumentRepository.findAll();

        return documents.stream()
                .map(userDocumentMapper::toDto)
                .toList();
    }
}

