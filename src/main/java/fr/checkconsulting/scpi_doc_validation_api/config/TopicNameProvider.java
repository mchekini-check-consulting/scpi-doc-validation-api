package fr.checkconsulting.scpi_doc_validation_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static fr.checkconsulting.scpi_doc_validation_api.utils.Constants.DOCUMENT_VALIDATION_TOPIC;
import static fr.checkconsulting.scpi_doc_validation_api.utils.Constants.SCPI_DOC_VALIDATION_GROUP;

@Component
public class TopicNameProvider {

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    public String getDocumentValidationTopic() {
        return DOCUMENT_VALIDATION_TOPIC + activeProfile;
    }

    public String getGroupId() {
        return SCPI_DOC_VALIDATION_GROUP + activeProfile;
    }
}
