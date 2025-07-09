package springevidence.models.dto.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import springevidence.data.entities.Insurance;
import springevidence.models.dto.InsuranceDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-09T02:31:34+0200",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class InsuranceMapperImpl implements InsuranceMapper {

    @Override
    public InsuranceDTO toDTO(Insurance insurance) {
        if ( insurance == null ) {
            return null;
        }

        InsuranceDTO insuranceDTO = new InsuranceDTO();

        insuranceDTO.setId( insurance.getId() );
        insuranceDTO.setValidFrom( insurance.getValidFrom() );
        insuranceDTO.setValidTo( insurance.getValidTo() );

        return insuranceDTO;
    }

    @Override
    public Insurance toEntity(InsuranceDTO insuranceDTO) {
        if ( insuranceDTO == null ) {
            return null;
        }

        Insurance insurance = new Insurance();

        insurance.setValidFrom( insuranceDTO.getValidFrom() );
        insurance.setValidTo( insuranceDTO.getValidTo() );

        return insurance;
    }
}
