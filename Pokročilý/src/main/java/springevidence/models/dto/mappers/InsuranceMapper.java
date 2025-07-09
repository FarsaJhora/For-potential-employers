package springevidence.models.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import springevidence.data.entities.Insurance;
import springevidence.models.dto.InsuranceDTO;

/**
 * Mapper pro převod mezi entitou Insurance a jejím DTO.
 * Používá MapStruct pro automatickou konverzi mezi objekty.
 */
@Mapper(componentModel = "spring")
public interface InsuranceMapper {

    InsuranceMapper INSTANCE = Mappers.getMapper(InsuranceMapper.class);

    InsuranceDTO toDTO(Insurance insurance);

    Insurance toEntity(InsuranceDTO insuranceDTO);
}