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

    private final KafkaProducerService kafkaProducerService;

    public DocumentService(UserDocumentRepository userDocumentRepository,
                           UserDocumentMapper userDocumentMapper, KafkaProducerService kafkaProducerService) {
        this.userDocumentRepository = userDocumentRepository;
        this.userDocumentMapper = userDocumentMapper;
        this.kafkaProducerService = kafkaProducerService;
    }

    public void saveDocument(UserDocumentDto dto) {

        if (dto == null) return;

        UserDocument entity = userDocumentRepository.findById(dto.getId())
                .orElseGet(UserDocument::new);

        entity.setId(dto.getId());
        entity.setUserEmail(dto.getUserEmail());
        entity.setFullName(dto.getFullName());
        entity.setType(dto.getType());
        entity.setStatus(UNDER_REVIEW);
        entity.setOriginalFileName(dto.getOriginalFileName());
        entity.setStoredFileName(dto.getStoredFileName());
        entity.setBucketName(dto.getBucketName());
        entity.setUploadedAt(dto.getUploadedAt());
        entity.setLastUpdatedAt(dto.getLastUpdatedAt());

        userDocumentRepository.save(entity);
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

        List<UserDocument> documents = userDocumentRepository.findAllById(ids);

        Map<String, DocumentStatus> newStatusMap = request.getDocuments().stream()
                .collect(Collectors.toMap(
                        UpdateItem::getId,
                        UpdateItem::getStatus
                ));

        documents.forEach(doc -> {
            DocumentStatus newStatus = newStatusMap.get(doc.getId());
            if (newStatus != null) {
                doc.setStatus(newStatus);
                doc.setLastUpdatedAt(LocalDateTime.now());
            }
        });

        List<UserDocument> saved = userDocumentRepository.saveAll(documents);

        List<UserDocumentDto> dtos = saved.stream()
                .map(userDocumentMapper::toDto)
                .toList();
        dtos.forEach(kafkaProducerService::sendDocumentStatus);

        return dtos;
    }


}
