package fr.checkconsulting.scpi_doc_validation_api.mapper;

import fr.checkconsulting.scpi_doc_validation_api.dto.UserDto;
import org.keycloak.representations.idm.UserRepresentation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    UserDto toDto(UserRepresentation userRepresentation);

    List<UserDto> toDto(List<UserRepresentation> userRepresentations);
}