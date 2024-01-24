package work.szczepanskimichal.userservice.mapper;

import org.mapstruct.Mapper;
import work.szczepanskimichal.userservice.entity.User;
import work.szczepanskimichal.userservice.entity.UserDto;
import work.szczepanskimichal.userservice.entity.UserUpdateDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserDto userDto);

    UserDto toUserDto(User user);

    User toEntity(UserUpdateDto userUpdateDto);

    UserUpdateDto toUserUpdateDto(User user);

}
