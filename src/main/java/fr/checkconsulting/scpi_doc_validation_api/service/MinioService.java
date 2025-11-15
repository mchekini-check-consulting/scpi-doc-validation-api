package fr.checkconsulting.scpi_doc_validation_api.service;

import fr.checkconsulting.scpi_doc_validation_api.model.entity.UserDocument;
import fr.checkconsulting.scpi_doc_validation_api.repository.UserDocumentRepository;
import io.minio.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@Slf4j
public class MinioService {

    private final String envPrefix;
    private final MinioClient minioClient;
    private final UserDocumentRepository userDocumentRepository;

    public MinioService(MinioClient minioClient,
                        UserDocumentRepository userDocumentRepository,
                        Environment environment) {
        this.minioClient = minioClient;
        this.userDocumentRepository = userDocumentRepository;

        String[] profiles = environment.getActiveProfiles();
        // tu peux adapter cette logique si besoin
        this.envPrefix = profiles.length > 0 ? profiles[0] : "default";

        log.info("MinioService initialisé avec envPrefix = {}", this.envPrefix);
    }

    public UserDocument getDocumentOrThrow(String id) {
        return userDocumentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document introuvable avec l'id : " + id));
    }

    private String resolveBucket(UserDocument doc) {
        return envPrefix + "-" + doc.getType().getDocumentType();
    }

    private String resolveObjectPath(UserDocument doc) {
        String bucket = resolveBucket(doc);
        String fileName = doc.getStoredFileName();

        Iterable<Result<Item>> items = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucket)
                        .recursive(true)
                        .build()
        );

        try {
            for (Result<Item> result : items) {
                Item item = result.get();
                if (item.objectName().endsWith("/" + fileName)) {
                    return item.objectName();
                }
            }
        } catch (Exception e) {
            log.error("Erreur lors de la recherche du fichier {}", fileName, e);
        }

        throw new IllegalStateException("Fichier introuvable dans MinIO : " + fileName);
    }

    public StatObjectResponse getMetadata(UserDocument doc) {
        try {
            String bucket = resolveBucket(doc);
            String objectPath = resolveObjectPath(doc);

            return minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectPath)
                            .build()
            );
        } catch (Exception e) {
            log.error("Erreur metadata MinIO pour {}", doc.getStoredFileName(), e);
            throw new IllegalStateException("Impossible d'obtenir les métadonnées du fichier", e);
        }
    }

    public byte[] downloadDocument(UserDocument doc) {
        try {
            String bucket = resolveBucket(doc);
            String objectPath = resolveObjectPath(doc);

            try (InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectPath)
                            .build()
            )) {
                return stream.readAllBytes();
            }

        } catch (Exception e) {
            log.error("Erreur lecture MinIO pour {}", doc.getStoredFileName(), e);
            throw new IllegalStateException("Erreur lors de la lecture du fichier dans MinIO", e);
        }
    }
}

