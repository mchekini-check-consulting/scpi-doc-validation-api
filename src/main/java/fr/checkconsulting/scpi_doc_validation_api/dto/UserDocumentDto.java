package fr.checkconsulting.scpi_doc_validation_api.dto;

import fr.checkconsulting.scpi_doc_validation_api.model.enums.DocumentStatus;
import fr.checkconsulting.scpi_doc_validation_api.model.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDocumentDto {

    private Long id;
    private String fullName;
    private String userEmail;
    private DocumentType type;
    private DocumentStatus status;
    private String originalFileName;
    private String storedFileName;
    private String bucketName;
    private LocalDateTime uploadedAt;
    private LocalDateTime lastUpdatedAt;

}
