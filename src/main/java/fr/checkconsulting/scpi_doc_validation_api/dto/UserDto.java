package fr.checkconsulting.scpi_doc_validation_api.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String role;
}
