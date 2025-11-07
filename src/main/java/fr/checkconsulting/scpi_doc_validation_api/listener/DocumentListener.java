package fr.checkconsulting.scpi_doc_validation_api.listener;

import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import fr.checkconsulting.scpi_doc_validation_api.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static fr.checkconsulting.scpi_doc_validation_api.utils.Constants.DOCUMENT_VALIDATION_TOPIC;
import static fr.checkconsulting.scpi_doc_validation_api.utils.Constants.SCPI_DOC_VALIDATION_GROUP;

@Component
public class DocumentListener {

    private static final Logger logger = LoggerFactory.getLogger(DocumentListener.class);

    private final DocumentService documentValidationService;

    public DocumentListener(DocumentService documentValidationService) {
        this.documentValidationService = documentValidationService;
    }

    @KafkaListener(
            topics = DOCUMENT_VALIDATION_TOPIC,
            groupId = SCPI_DOC_VALIDATION_GROUP
    )
    public void consume(UserDocumentDto documentDto) {
        logger.info("Document reçu depuis Kafka : {}", documentDto);
        documentValidationService.saveDocument(documentDto);
    }
}
