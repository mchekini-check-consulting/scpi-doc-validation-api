package fr.checkconsulting.scpi_doc_validation_api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStatusRequest {

    private String email;
    private List<UpdateItem> documents;
}



