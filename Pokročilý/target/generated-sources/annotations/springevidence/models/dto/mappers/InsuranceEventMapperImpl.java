package springevidence.models.dto.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import springevidence.data.entities.Insurance;
import springevidence.data.entities.InsuranceEvent;
import springevidence.models.dto.InsuranceEventDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-09T02:31:34+0200",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class InsuranceEventMapperImpl implements InsuranceEventMapper {

    @Override
    public InsuranceEventDTO toDTO(InsuranceEvent insuranceEvent) {
        if ( insuranceEvent == null ) {
            return null;
        }

        InsuranceEventDTO insuranceEventDTO = new InsuranceEventDTO();

        insuranceEventDTO.setInsuranceId( insuranceEventInsuranceId( insuranceEvent ) );
        insuranceEventDTO.setEventDate( insuranceEvent.getEventDate() );
        insuranceEventDTO.setId( insuranceEvent.getId() );
        insuranceEventDTO.setDescription( insuranceEvent.getDescription() );

        return insuranceEventDTO;
    }

    @Override
    public InsuranceEvent toEntity(InsuranceEventDTO insuranceEventDTO) {
        if ( insuranceEventDTO == null ) {
            return null;
        }

        InsuranceEvent insuranceEvent = new InsuranceEvent();

        insuranceEvent.setDescription( insuranceEventDTO.getDescription() );
        insuranceEvent.setEventDate( insuranceEventDTO.getEventDate() );

        return insuranceEvent;
    }

    private Long insuranceEventInsuranceId(InsuranceEvent insuranceEvent) {
        if ( insuranceEvent == null ) {
            return null;
        }
        Insurance insurance = insuranceEvent.getInsurance();
        if ( insurance == null ) {
            return null;
        }
        Long id = insurance.getId();
        if ( id == null ) {
            return null;
        }
        return id;
    }
}
