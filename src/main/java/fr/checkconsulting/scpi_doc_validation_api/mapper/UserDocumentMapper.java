package fr.checkconsulting.scpi_doc_validation_api.mapper;

import fr.checkconsulting.scpi_doc_validation_api.dto.UserDocumentDto;
import fr.checkconsulting.scpi_doc_validation_api.model.entity.UserDocument;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDocumentMapper {
    UserDocumentDto toDto(UserDocument entity);
    UserDocument toEntity(UserDocumentDto dto);
    List<UserDocumentDto> toDtoList(List<UserDocument> entities);

}
