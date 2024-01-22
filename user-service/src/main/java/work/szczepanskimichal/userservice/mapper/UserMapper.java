package work.szczepanskimichal.userservice.mapper;

import org.mapstruct.Mapper;
import work.szczepanskimichal.userservice.entity.User;
import work.szczepanskimichal.userservice.entity.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto userDto);

}
