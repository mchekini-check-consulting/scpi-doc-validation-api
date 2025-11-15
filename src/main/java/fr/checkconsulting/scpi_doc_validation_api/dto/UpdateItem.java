package fr.checkconsulting.scpi_doc_validation_api.dto;

import fr.checkconsulting.scpi_doc_validation_api.model.enums.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItem {

    private String id;
    private DocumentStatus status;

}
