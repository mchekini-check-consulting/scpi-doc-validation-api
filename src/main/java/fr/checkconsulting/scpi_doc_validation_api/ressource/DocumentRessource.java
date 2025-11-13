package fr.checkconsulting.scpi_doc_validation_api.ressource;

import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import fr.checkconsulting.scpi_doc_validation_api.service.DocumentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentRessource {

    private final DocumentService documentService;

    public DocumentRessource(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public List<UserDocumentDto> getAllDocuments() {
        return documentService.findAllDocuments();
    }
}
