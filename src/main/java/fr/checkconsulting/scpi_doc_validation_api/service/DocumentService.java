package fr.checkconsulting.scpi_doc_validation_api.service;

import fr.checkconsulting.scpi_doc_validation_api.dto.UpdateItem;
import fr.checkconsulting.scpi_doc_validation_api.dto.UpdateStatusRequest;
import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import fr.checkconsulting.scpi_doc_validation_api.mapper.UserDocumentMapper;
import fr.checkconsulting.scpi_doc_validation_api.model.entity.UserDocument;
import fr.checkconsulting.scpi_doc_validation_api.model.enums.DocumentStatus;
import fr.checkconsulting.scpi_doc_validation_api.repository.UserDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static fr.checkconsulting.scpi_doc_validation_api.model.enums.DocumentStatus.UNDER_REVIEW;

@Service
public class DocumentService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    private final UserDocumentRepository userDocumentRepository;
    private final UserDocumentMapper userDocumentMapper;

    public DocumentService(UserDocumentRepository userDocumentRepository,
                           UserDocumentMapper userDocumentMapper) {
        this.userDocumentRepository = userDocumentRepository;
        this.userDocumentMapper = userDocumentMapper;
    }

    public void saveDocument(UserDocumentDto dto) {
        if (dto == null) {
            logger.info("Le document reçu est null, sauvegarde ignorée.");
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

        logger.info("Document sauvegardé pour {}", dto.getUserEmail());
    }

    public Page<UserDocumentDto> findAllDocuments(Pageable pageable) {
        return userDocumentRepository.findAll(pageable)
                .map(userDocumentMapper::toDto);
    }

    public List<UserDocumentDto> getDocumentsByUserEmail(String email) {
        return userDocumentMapper.toDtoList(
                userDocumentRepository.findByUserEmail(email)
        );
    }

    public UserDocumentDto getDocumentById(String id) {
        UserDocument doc = userDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document introuvable."));
        return userDocumentMapper.toDto(doc);
    }

    @Transactional
    public List<UserDocumentDto> updateStatus(UpdateStatusRequest request) {

        List<String> ids = request.getDocuments().stream()
                .map(UpdateItem::getId)
                .toList();

        List<UserDocument> docs = userDocumentRepository.findAllById(ids);

        Map<String, DocumentStatus> newStatusMap = request.getDocuments().stream()
                .collect(Collectors.toMap(
                        UpdateItem::getId,
                        UpdateItem::getStatus
                ));

        docs.forEach(doc -> {
            DocumentStatus newStatus = newStatusMap.get(doc.getId());
            if (newStatus != null) {
                doc.setStatus(newStatus);
                doc.setLastUpdatedAt(LocalDateTime.now());
            }
        });

        List<UserDocument> saved = userDocumentRepository.saveAll(docs);

        logger.info("Mise à jour de {} documents", saved.size());

        return saved.stream()
                .map(userDocumentMapper::toDto)
                .toList();
    }


}
