package fr.checkconsulting.scpi_doc_validation_api.service;

import fr.checkconsulting.scpi_doc_validation_api.config.TopicNameProvider;
import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TopicNameProvider topicNameProvider;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate, TopicNameProvider topicNameProvider) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicNameProvider = topicNameProvider;
    }

    public void sendDocumentStatus(UserDocumentDto dto) {
        String topic = topicNameProvider.getDocumentValidationResponseTopic();
        log.info("Envoi de la réponse sur le topic [{}] : {}", topic, dto);
        kafkaTemplate.send(topic, dto);
    }
}