package fr.checkconsulting.scpi_doc_validation_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
