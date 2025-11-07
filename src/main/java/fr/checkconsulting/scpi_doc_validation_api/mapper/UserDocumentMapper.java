package fr.checkconsulting.scpi_doc_validation_api.mapper;

import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import fr.checkconsulting.scpi_doc_validation_api.model.entity.UserDocument;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDocumentMapper {

    UserDocument toEntity(UserDocumentDto dto);

    UserDocumentDto toDto(UserDocument entity);
}
