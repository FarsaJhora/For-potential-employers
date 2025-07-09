package springevidence.models.dto.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import springevidence.data.entities.InsuranceEvent;
import springevidence.models.dto.InsuranceEventDTO;

/**
 * Mapper pro převod mezi entitou InsuranceEvent a jejím DTO.
 * Používá MapStruct pro automatickou konverzi mezi objekty.
 */
@Mapper(componentModel = "spring")
public interface InsuranceEventMapper {

    InsuranceEventMapper INSTANCE = Mappers.getMapper(InsuranceEventMapper.class);

    @Mapping(source = "insurance.id", target = "insuranceId")
    InsuranceEventDTO toDTO(InsuranceEvent insuranceEvent);

    InsuranceEvent toEntity(InsuranceEventDTO insuranceEventDTO);

    default InsuranceEvent mapInsuranceEventDTOToEntity(InsuranceEventDTO dto) {
        if (dto == null) {
            return null;
        }
        InsuranceEvent entity = new InsuranceEvent();
        
        if (dto.getInsuranceId() != null) {
            springevidence.data.entities.Insurance insurance = new springevidence.data.entities.Insurance();
            
            try {
                java.lang.reflect.Field idField = insurance.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(insurance, dto.getInsuranceId());
            } catch (Exception e) {
                
            }
            entity.setInsurance(insurance);
        }
        entity.setEventDate(dto.getEventDate());
        entity.setDescription(dto.getDescription());
        return entity;
    }
}
