package springevidence.models.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import springevidence.data.entities.User;
import springevidence.models.dto.UserDTO;

/**
 * Mapper pro převod mezi entitou User a jejím DTO.
 * Používá MapStruct pro automatickou konverzi mezi objekty.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);

    User toEntity(UserDTO userDTO);
}
