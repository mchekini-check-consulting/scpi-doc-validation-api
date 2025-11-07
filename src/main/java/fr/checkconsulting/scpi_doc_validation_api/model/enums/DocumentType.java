package fr.checkconsulting.scpi_doc_validation_api.model.enums;

import lombok.Getter;

@Getter
public enum DocumentType {

    AVIS_IMPOSITION("avis-imposition"),
    PIECE_IDENTITE("piece-identite"),
    JUSTIFICATIF_DOMICILE("justificatif-domicile");

    private final String documentType;

    DocumentType(String documentType) {
        this.documentType = documentType;
    }
}
