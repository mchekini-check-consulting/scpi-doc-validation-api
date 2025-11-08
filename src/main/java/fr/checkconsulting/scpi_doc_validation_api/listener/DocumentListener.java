package fr.checkconsulting.scpi_doc_validation_api.listener;

import fr.checkconsulting.scpi_doc_validation_api.config.TopicNameProvider;
import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import fr.checkconsulting.scpi_doc_validation_api.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DocumentListener {

    private static final Logger log = LoggerFactory.getLogger(DocumentListener.class);

    private final DocumentService documentService;
    private final TopicNameProvider topicNameProvider;

    public DocumentListener(DocumentService documentService, TopicNameProvider topicNameProvider) {
        this.documentService = documentService;
        this.topicNameProvider = topicNameProvider;
    }

    @KafkaListener(
            topics = "document-validation-topic-${spring.profiles.active:local}",
            groupId = "scpi-doc-validation-group-${spring.profiles.active:local}"
    )
    public void consume(UserDocumentDto documentDto) {
        log.info(
                "Document reçu depuis Kafka sur le topic [{}] : {}",
                topicNameProvider.getDocumentValidationTopic(),
                documentDto
        );
        documentService.saveDocument(documentDto);
    }
}
