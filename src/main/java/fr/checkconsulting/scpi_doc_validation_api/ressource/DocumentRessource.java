package fr.checkconsulting.scpi_doc_validation_api.ressource;

import fr.checkconsulting.scpi_doc_validation_api.dto.UpdateStatusRequest;
import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import fr.checkconsulting.scpi_doc_validation_api.model.entity.UserDocument;
import fr.checkconsulting.scpi_doc_validation_api.service.DocumentService;
import fr.checkconsulting.scpi_doc_validation_api.service.MinioService;
import io.minio.StatObjectResponse;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentRessource {

    private final DocumentService documentService;
    private final MinioService minioService;

    public DocumentRessource(DocumentService documentService,
                             MinioService minioService) {
        this.documentService = documentService;
        this.minioService = minioService;
    }

    @GetMapping
    public Page<UserDocumentDto> getDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return documentService.findAllDocuments(pageable);
    }

    @GetMapping("/by-email")
    public List<UserDocumentDto> getByEmail(
            @RequestParam String email
    ) {
        return documentService.getDocumentsByUserEmail(email);
    }

    @GetMapping("/{id}")
    public UserDocumentDto getDocumentById(@PathVariable String id) {
        return documentService.getDocumentById(id);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<ByteArrayResource> download(
            @PathVariable String id,
            @RequestParam(defaultValue = "false") boolean attachment) {

        UserDocument doc = minioService.getDocumentOrThrow(id);
        byte[] data = minioService.downloadDocument(doc);
        StatObjectResponse metadata = minioService.getMetadata(doc);

        String contentType = Optional.ofNullable(metadata.contentType())
                .orElse(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        String fileName = Optional.ofNullable(doc.getOriginalFileName())
                .orElse("document");

        String encoded = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, contentType);
        headers.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        headers.set(HttpHeaders.CONTENT_DISPOSITION,
                (attachment ? "attachment" : "inline")
                        + "; filename=\"" + fileName
                        + "\"; filename*=UTF-8''" + encoded);

        headers.set("X-Frame-Options", "ALLOWALL");
        headers.set("Content-Security-Policy", "frame-ancestors *");

        return ResponseEntity.ok().headers(headers).body(new ByteArrayResource(data));
    }

    @PatchMapping("/status/update")
    public ResponseEntity<List<UserDocumentDto>> updateStatus(
            @RequestBody UpdateStatusRequest request
    ) {
        return ResponseEntity.ok(documentService.updateStatus(request));
    }


}
