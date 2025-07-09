package springevidence.models.dto.mappers;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import springevidence.data.entities.User;
import springevidence.data.entities.UserRole;
import springevidence.models.dto.UserDTO;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-09T02:31:34+0200",
    comments = "version: 1.5.3.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO userDTO = new UserDTO();

        userDTO.setId( user.getId() );
        userDTO.setEmail( user.getEmail() );
        if ( user.getRole() != null ) {
            userDTO.setRole( user.getRole().name() );
        }

        return userDTO;
    }

    @Override
    public User toEntity(UserDTO userDTO) {
        if ( userDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userDTO.getId() );
        user.setEmail( userDTO.getEmail() );
        if ( userDTO.getRole() != null ) {
            user.setRole( Enum.valueOf( UserRole.class, userDTO.getRole() ) );
        }

        return user;
    }
}
